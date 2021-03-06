import * as React from "react";
import * as ReactDOM from "react-dom";
import Popup from 'react-popup';
import {MapView} from './map.js'
import {registerErrorPrompt} from 'common-components/lib/register.js'

import 'common-components/css/coalesce.css'
import 'common-components/css/popup.css'
import './index.css'

var rootUrl;

if (window.location.port == 3000) {
  rootUrl  = 'http://' + window.location.hostname + ':8181';
} else {
  rootUrl  = '';
}

registerErrorPrompt(Popup);

class App extends React.Component {

  constructor(props) {
    super(props);

    this.state = props;
  }

fetchCapabilities(url) {

  fetch(url + '/wfs?service=wfs&version=1.1.0&request=GetCapabilities', {

  })
    .then(res => res.text())
    .then(str => (new window.DOMParser()).parseFromString(str, "text/xml"))
    .then(doc => {

      //const {availableLayers} = this.state;

      console.log(doc);
      var featureTypes = doc.getElementsByTagName("FeatureTypeList")[0].getElementsByTagName("FeatureType");

      console.log('(' + featureTypes.length  + ') Layers Loaded');

      var layers = [];

      for (var ii=0; ii<featureTypes.length; ii++) {

        var title = featureTypes[ii].getElementsByTagName("Title")[0].innerHTML;
        var name = featureTypes[ii].getElementsByTagName("Name")[0].innerHTML;

        layers.push({name: title, key: title});

      }

      layers.sort(function(a, b) {
                var nameA = a.name.toUpperCase();
                var nameB = b.name.toUpperCase();
                if (nameA < nameB) {
                  return -1;
                }
                if (nameA > nameB) {
                  return 1;
                }

                return 0;
              }).forEach(function(layer) {
                // TODO Populate availableLayers
                //console.log(layer.name);
              })
    }).catch(function(error) {
      Popup.plugins().promptError("(FAILED) Loading Capabilities: " + error);
    })

  }

  fetchLayers(url) {
    fetch(url + '/rest/layers.json')
      .then(res => res.json())
      .then(data => {

        const {availableLayers} = this.state;

        // Clear Values
        availableLayers.slice(0, availableLayers.length);

        console.log('(' + data.layers.layer.length  + ') Layers Loaded');

        data.layers.layer.sort(function(a, b) {
                  var nameA = a.name.toUpperCase();
                  var nameB = b.name.toUpperCase();
                  if (nameA < nameB) {
                    return -1;
                  }
                  if (nameA > nameB) {
                    return 1;
                  }

                  return 0;
                }).forEach(function(layer) {
                  availableLayers.push(layer);
                })

        this.setState({
          availableLayers: availableLayers
        });

      }).catch(function(error) {
        Popup.plugins().promptError("(FAILED) Loading Layers: " + error);
      })
  }

  fetchStyles(url) {

    var that = this;

    var query = {
      "pageSize": 200,
      "pageNumber": 1,
      "propertyNames": [
        "meta.stylename",
        "Fill Style.color",
        "Fill Style.alpha",
        "Stroke Style.color",
        "Stroke Style.width",
        "Text Style.fontSize",
        "Text Style.fontType",
        "Text Style.fontFillColor",
        "Text Style.fontStrokeColor",
        "Text Style.fontStrokeWidth",
      ],
      "group": {
        "booleanComparer": "AND",
        "criteria": [{
          "recordset": "coalesceentity",
          "field": "name",
          "comparer": "=",
          "value": "Style",
          "matchCase": "true"
        }]
      }
    };

    fetch(url + '/cxf/data/search/complex', {
      method: "POST",
      body: JSON.stringify(query),
      headers: new Headers({
        'content-type': 'application/json; charset=utf-8'
      }),
    }).then(res => res.json())
      .then(data => {

        const {styles} = that.state;

        if (data.result.length === 1 && data.result[0].status === 'SUCCESS') {
          data.result[0].result.hits.forEach(function (hit) {
            styles.push({
                "key": hit.values[0],
                "name": hit.values[0],
                "fill": {
                  "color": hit.values[1],
                  "alpha": hit.values[2]
                },
                "stroke": {
                  "color": hit.values[3],
                  "width": hit.values[4]
                },
                "text": {
                  "font": {
                    "size": hit.values[5],
                    "type": hit.values[6]
                  },
                  "fill": {
                    "color": hit.values[7]
                  },
                  "stroke": {
                    "color": hit.values[8],
                    "width": hit.values[9]
                  }
                }
            });

          });
        }


        that.setState({
          styles: styles
        });
      }).catch(function(error) {
        Popup.plugins().promptError('(FAILED) Loading Styles: ' + JSON.stringify(error));
      });
  }

  componentDidMount() {
    this.fetchLayers(this.state.geoserver);
    this.fetchStyles(this.state.karafserver);
    //this.fetchCapabilities(this.state.geoserver);
  }

  render() {
    return (
        <MapView
          geoserver={this.state.geoserver}
          workspace={this.state.workspace}
          styles={this.state.styles}
          availableLayers={this.state.availableLayers}
          selectedLayers={this.state.selectedLayers}
          />
    )
  }

}

App.defaultProps = {
  geoserver: 'http://bdpgeoserver.bdpdev.incadencecorp.com:8181/geoserver',
  workspace: 'OE_Repository',
  karafserver: rootUrl,
  availableLayers: [],
  styles: [],
  // TODO Default layers should be removed for production or load from a saved state.
  selectedLayers: [
    {
      key: 'OSM',
      name: 'OSM',
      type: 'WMS',
      checked: false
    },
    {
      key: 'NaturalEarth',
      name: 'NaturalEarth',
      type: 'WMS',
      checked: true
    },
    {
      key: 'cib1',
      name: 'cib1',
      type: 'WMS',
      checked: true
    },
    {
      key: 'OSM_buildings',
      name: 'OSM_buildings',
      type: 'WMS',
      checked: false
    },
    {
      key: 'OSM_highways_line',
      name: 'OSM_highways_line',
      type: 'WMS',
      checked: true
    },
    {
      key: 'OSM_highways_polygon',
      name: 'OSM_highways_polygon',
      type: 'WMS',
      checked: true
    },
    {
      key: 'waterway_line',
      name: 'waterway_line',
      type: 'WMS',
      checked: true
    }, {
      key: 'waterway_polygon',
      name: 'waterway_polygon',
      type: 'WMS',
      checked: true
    }
  ]
}

fetch(rootUrl + '/cxf/data/property/geoserver.url')
  .then(res => res.text())
  .then(data => {
    ReactDOM.render(
      <App geoserver={data}/>,
      document.getElementById('main')
    );
}).catch(function(error) {
  Popup.plugins().promptError("(FAILED) Retrieving Geo Server URL: " + error);
  ReactDOM.render(
    <App />,
    document.getElementById('main')
  );
});

ReactDOM.render(
  <Popup />,
  document.getElementById('popupContainer')
);
