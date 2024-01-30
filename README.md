
# Seam Carving  in Java
This project implements a dynamic programming solution for the Seam Carving algorithm, as part of an assignment for the Algorithms and Data Structures II course by Princeton University.
Dynamic programming is employed to efficiently compute the optimal seams to remove from the image. The algorithm identifies and removes the seams of least importance iteratively, ensuring that the resized image maintains the most relevant content.

[![wakatime](https://wakatime.com/badge/user/1573cfda-f106-4c1b-87c2-64cdbd982d7a/project/018d47c9-362d-41ed-8e70-d969b82195ee.svg)](https://wakatime.com/badge/user/1573cfda-f106-4c1b-87c2-64cdbd982d7a/project/018d47c9-362d-41ed-8e70-d969b82195ee)
## Usage
To use this implementation, follow these steps:

Clone the repository to your local machine:
```bash
git clone[ https://github.com/your-username/seam-carving-java.git](https://github.com/ahmedSherif-eng/SeamCarver.git)
```
Compile the Java files:
```bash
javac ResizeDemo.java example.png  50 100 (example)
```
```bash
Copy code
java SeamCarving input.jpg 400 300
```
This will resize the input image input.jpg to have a width of 400 pixels and a height of 300 pixels.
Run the program with the input image file path and desired number of Vertical and Horizontal number of eliminated **Seams**:

### Alternatively
you can give the image name in class *ResizeDemo.java*, number of columns and number of rows  
![image](https://github.com/ahmedSherif-eng/SeamCarver/assets/72231218/cc7203a4-58f7-440a-9ec0-911575ea4a67)



## Showcase
![Input Image](https://github.com/ahmedSherif-eng/SeamCarver/assets/72231218/ead0be0f-80f5-4c23-91c4-12970c6ad5b9)


![Output Image
](https://github.com/ahmedSherif-eng/SeamCarver/assets/72231218/84394e9c-6a91-4ff3-920a-85d9a2f7ca76)

## References
Algorithms and data structure course provided by Princeton University on Coursera platform.
####
This project is licensed under the MIT License - see the LICENSE file for details.
