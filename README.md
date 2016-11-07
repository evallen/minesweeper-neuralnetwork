# minesweeper-neuralnetwork
This project is just a small way for me to get myself acquainted with the structure of a neural network, using a genetic algorithm to improve it. It's based off of the great tutorial at http://www.ai-junkie.com/ann/evolved/nnt1.html, although I modified a lot of it and the code is nearly all new Java. 

Essentially, the program consists of a small arena where little "MineSweepers" drive about. There are many mines dispersed throughout, which can either raise or lower the scores of the MineSweepers. The objective is for them to be able to recognize the mines and act accordingly to maximize their score. To do this, each MineSweeper is equipped with a neural network. After each round, a genetic algorithm dipatches the worst MineSweepers and makes a new population, starting again. Over time they are in fact able to improve. 

There is a graph (made by the JFreeChart library) that displays the history of the population's fitness. 
