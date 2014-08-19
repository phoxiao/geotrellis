package geotrellis.spark.tiling

import geotrellis.raster._
import geotrellis.proj4._
import geotrellis.vector.Extent
import geotrellis.vector.reproject._

object TilingScheme {
  /** Default tiling scheme for WSG84 */
  def GEODETIC = 
    new TmsTilingScheme(LatLng, 512)
}

trait TilingScheme {
  val extent: Extent
  val crs: CRS

  def zoomLevelFor(cellSize: CellSize): ZoomLevel
  def zoomLevel(level: Int): ZoomLevel
}
