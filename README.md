# Pexel Perfect
#### Grace Chen, 6/21/18

Created as a small personal project to practice web scraping with Python and to explore some useful Java libraries.

### Running the program
`java GeneratePexel <filename>`

pexel.py can be run as a standalone script to generate a random image URL from Pexels with 

`python pexel.py <search term>`.

### Purpose 
Strips out common words from the given text file, generates a stock image from Pexels for each of 0-20% of the most important words, and downloads the stock images (with .jpg extension) to the user's computer under /pexel_imgs. Takes about 3 minutes to process 1k words.

Images are named with the convention of
`wordOrderInText_pexelSearchKeyword.jpg`

Example: `0_while.jpg`

---

### Example Output:
![Running GeneratePexel](https://imgur.com/jft1ZMQ.jpg "Completed output")
![Generated images in directory](https://imgur.com/w4ZQguw.jpg "Generated Pexel images")
