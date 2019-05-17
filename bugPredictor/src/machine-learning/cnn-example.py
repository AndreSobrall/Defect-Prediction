from keras.datasets import mnist
from keras.utils import to_categorical
from keras.models import Sequential
from keras.layers import Dense, Conv2D, Flatten
import matplotlib.pyplot as plt

# ----------------------------------------------- #
# 		Dataset Loading & Pre-processing 	  	  #
# ----------------------------------------------- #

	#download mnist data and split into train and test sets
	(X_train, y_train), (X_test, y_test) = mnist.load_data()

	#plot the first image in the dataset
	plt.imshow(X_train[0])

	#check image shape
	X_train[0].shape

	# Data pre-processing
	# Next, we need to reshape our dataset inputs (X_train and X_test) 
	# to the shape that our model expects when we train the model.
	# The first number is the number of images 
	# (60,000 for X_train and 10,000 for X_test). 
	# Then comes the shape of each image (28x28). 
	# The last number is 1, which signifies that the images are greyscale.
	#reshape data to fit model
	X_train = X_train.reshape(60000,28,28,1)
	X_test  = X_test.reshape(10000,28,28,1)

	# We need to ‘one-hot-encode’ our target variable. 
	#This means that a column will be created for each output category and a binary variable is inputted for each category.
	# For example, we saw that the first image in the dataset is a 5. 
	# This means that the sixth number in our array will have a 1 and the rest of the array will be filled with 0.

	#one-hot encode target column
	y_train = to_categorical(y_train)
	y_test  = to_categorical(y_test)
	y_train[0]


# --------------------------- #
# 		Building the model 	  #
# --------------------------- #

# The model type that we will be using is Sequential. 
# Sequential is the easiest way to build a model in Keras. It allows you to build a model layer by layer.

#create model
model = Sequential()

# We use the ‘add()’ function to add layers to our model.
# Our first 2 layers are Conv2D layers. These are convolution layers that will deal with our input images, which are seen as 2-dimensional matrices.
# 64 in the first layer and 32 in the second layer are the number of nodes in each layer. 
# This number can be adjusted to be higher or lower, depending on the size of the dataset. 
# In our case, 64 and 32 work well, so we will stick with this for now.
#a dd model layers
model.add(Conv2D(64, kernel_size=3, activation='relu', input_shape=(28,28,1)))
model.add(Conv2D(32, kernel_size=3, activation='relu'))
model.add(Flatten())
model.add(Dense(10, activation='softmax'))

# Kernel size is the size of the filter matrix for our convolution. 
# So a kernel size of 3 means we will have a 3x3 filter matrix. 
# Refer back to the introduction and the first image for a refresher on this.

# Activation is the activation function for the layer. 
# The activation function we will be using for our first 2 layers is the ReLU, or Rectified Linear Activation.
# This activation function has been proven to work well in neural networks.

# In between the Conv2D layers and the dense layer, there is a ‘Flatten’ layer. 
# Flatten serves as a connection between the convolution and dense layers.

# ‘Dense’ is the layer type we will use in for our output layer. 
# Dense is a standard layer type that is used in many cases for neural networks.
# We will have 10 nodes in our output layer, one for each possible outcome (0–9).

# The activation is ‘softmax’. 
# Softmax makes the output sum up to 1 so the output can be interpreted as probabilities. 
# The model will then make its prediction based on which option has the highest probability.

# --------------------------- #
# 	  Compiling the model 	  #
# --------------------------- #

# Compiling the model takes three parameters: optimizer, loss and metrics.

# The optimizer controls the learning rate. 
# We will be using ‘adam’ as our optmizer. 
# Adam is generally a good optimizer to use for many cases. 
# The adam optimizer adjusts the learning rate throughout training.

# The learning rate determines how fast the optimal weights for the model are calculated. 
# A smaller learning rate may lead to more accurate weights (up to a certain point), but the time it takes to compute the weights will be longer.

# We will use ‘categorical_crossentropy’ for our loss function. 
# This is the most common choice for classification. 
# A lower score indicates that the model is performing better.

# We will use the ‘accuracy’ metric to see the accuracy score on the validation set when we train the model.

# Compile model using accuracy to measure model performance
model.compile(optimizer='adam', 
			  loss='categorical_crossentropy', 
			  metrics=['accuracy'])


# --------------------------- #
# 	  Trainning the model 	  #
# --------------------------- #

# To train, we will use the ‘fit()’ function on our model with the following parameters: 
# training data (train_X), target data (train_y), validation data, and the number of epochs.

# For our validation data, we will use the test set provided to us in our dataset, 
# which we have split into X_test and y_test.

# he number of epochs is the number of times the model will cycle through the data. 
# The more epochs we run, the more the model will improve, up to a certain point. 
# After that point, the model will stop improving during each epoch. 
# For our model, we will set the number of epochs to 3.
# train the model
model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=3)






