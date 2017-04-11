# image-morphing
Image Processing desktop app implemented using JavaFX and SwingUI.

Algorithm:
------------------

Morphing process is based on triangulation, using this approach:

  1. Initial and target image are subdivided into triangles. 
  2. User interacts with the Mesh control points and defines Morphing Mesh coordinates. 
  3. Morph process starts individiually for each triangle using the amount of interpolation α.
  4. Map each R, G and B component from the initial pixel to the R, G and B component of the target pixel
  5. Combine all the triangles to the final image 

Project structure:
------------------

![alt tag](https://github.com/hideburn/image-morphing/blob/master/docs/imgs/structure.png)
