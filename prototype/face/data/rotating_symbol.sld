<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
 xmlns="http://www.opengis.net/sld"
 xmlns:ogc="http://www.opengis.net/ogc"
 xmlns:xlink="http://www.w3.org/1999/xlink"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>default_point</Name>
    <UserStyle>
      <Title>Rotating Triangle</Title>
      <Abstract>Draws a triangle with a direction marker, then rotates based on a property.</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- A FeatureTypeStyle for rendering points -->
      <FeatureTypeStyle>
        <Rule>
          <Name>Triangle</Name>
	  <PointSymbolizer>
              <Graphic>
	        <ExternalGraphic>
                  <OnlineResource
                    xlink:type="simple"
                    xlink:href="file://triangle.png" />
                  <Format>image/png</Format>
                </ExternalGraphic>
              <Rotation><ogc:PropertyName>Angle</ogc:PropertyName></Rotation>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>

