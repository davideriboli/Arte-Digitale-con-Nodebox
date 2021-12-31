# nodebox3-svg-colorizer
Colorize a svg map from a data list.


You have to re-assign the path of the libraries (include folder).

#-----------------------------------------------------------------------

data.json :

values: list of items key->region, value->integer
colors: range of colors, with the min. and the max. value.

#-----------------------------------------------------------------------

Node: NET_SORT_LIST is a network to sort the items of the data.json
In this networl the is a sub-item for each region. Just pay atention at
the output of the region list and compare it with the svg file blocks:
  g/paths -> id
