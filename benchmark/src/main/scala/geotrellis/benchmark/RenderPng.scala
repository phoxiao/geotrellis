package geotrellis.benchmark

import geotrellis._
import geotrellis.source._
import geotrellis.raster.op.global
import geotrellis.statistics.op.stat._
import geotrellis.io._
import geotrellis.render.op._

import com.google.caliper.Param

object RenderPngBenchmark extends BenchmarkRunner(classOf[RenderPngBenchmark])
class RenderPngBenchmark extends OperationBenchmark {
  val n = 10
  val name = "SBN_farm_mkt"
  val colors = Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000, 0xFF0000)

  @Param(Array("256","512", "1024", "2048", "4096", "8192"))
  var size:Int = 0

  var op:Op[Png] = null
  var source:ValueSource[Png] = null
  override def setUp() {
    val re = getRasterExtent(name, size, size)
    val raster = get(io.LoadRaster(name,re))
    op =
      GetHistogram(raster).flatMap { h =>
        val breaksOp = GetColorBreaks(h, colors)
        RenderPng(raster, breaksOp, 0, h)
      }

    source = 
      RasterSource(name,re)
        .cached
        .renderPng(colors)
  }

  def timeRenderPngOp(reps:Int) = run(reps)(renderPngOp)
  def renderPngOp = get(op)

  def timeRenderPngSource(reps:Int) = run(reps)(renderPngSource)
  def renderPngSource = get(source)
}
