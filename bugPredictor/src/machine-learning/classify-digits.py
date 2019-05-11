from keras.datasets import mnist
from keras import models 
from keras import layers
from keras.utils import to_categorical

(train_images, train_labels), (test_images, test_labels) = mnist.load_data()
# train_images e train_labels -> trainning dataset
# test_images, test_labels -> test dataset


# Defines ML model, number of layers and expected input
network = models.Sequential() 
network.add(layers.Dense(512, activation='relu', input_shape=(28 * 28,)))
# here defines de output layer configuration 
network.add(layers.Dense(10, activation='softmax'))

#defines the optimizer, the loss function, and the evaluation metrics.
network.compile(optimizer='rmsprop',
				loss='categorical_crossentropy', 
				metrics=['accuracy'])

# Data preprocessing step

# Preparing the image data
train_images = train_images.reshape((60000, 28 * 28))
train_images = train_images.astype('float32') / 255

test_images = test_images.reshape((10000, 28 * 28))
test_images = test_images.astype('float32') / 255

# Categorically enconde the labels
train_labels = to_categorical(train_labels)
test_labels = to_categorical(test_labels)

# Now we're ready to train the netwrok
network.fit(train_images, train_labels, epochs=5, batch_size=128)
# Two quantities are displayed during training: 
# the loss of the network over the training data, 
# and the accuracy of the network over the training data.
