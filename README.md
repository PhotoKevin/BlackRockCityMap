# Black Rock City Map
Generate maps of Black Rock City from data files in SVG (Inkscape) and KML (Google Earth) formats.
Covers 2014 to 2023.

### Note
You don't need to build or run the program yourself. The generated maps are attached to the releases. 

### License
The code is licensed as the MIT 2 clause but the generated maps are CC0 (Public domain). 

## Background
This started out because I wanted to see how Black Rock City compared to other cities in Google Earth. 
By writing a program that generated KML files I could easily shift the city location and regenerate. Initially
I thought I could completely generate the maps from the CSV files supplied by the Burning Man organization but
it turned out that the data was incomplete and the tags varied slightly from year to year. Still, I got
something that worked well enough and dropped the project for a long time. 

A couple years later I was seeing people ask for SVG files so that they could make gifts. You can find various
such on the interwebs but being a bit OCD sometimes, I noticed that they didn't identify what year the maps
were for and I was uncertain as to their accuracy. So I went back to my program and revamped it to do SVG and filled 
in a lot of details that I had ignored before. In doing so I got tired of continually loading the SVG file
into Inkscape so that I could see what it was looking like and put a basic GUI on it.

## SVG Files
As noted, this program generates SVG files for InkScape. InkScape is 100% free a vector drawing package. 
Since it's vector based, the images can be scaled to any degree you like without losing quality.
If you need a bitmap, Inkscape can export PNG files in any resolution you like.

## The math
Laying out the city requires a good bit of trigonometry. There are SVG files in the source tree that give 
some of the math using diagrams. Even so, they're terse but they're what I made in working things out.

## Errors and Ommisions
In 2014 some of the roads were different widths, and some of the plazas were different diameters. This
is not reflected in the output. Later years had all of the roads and plazas the same size.

Some of the caclulations assume short road lengths are straight when they are, in fact, curved. This probably makes 
for errors of a few inches.

The sizes of the circles around the Man and the Temple are taken from Google Earth measurements. The Burning Man
org does not give these sizes in any data set that I've been able to find.

