# Objetivo de utilizacao da CNN:
# - Feature extraction
#-----------
# Feature extraction starts from an initial set of measured data
# It builds derived values (features) intended to be informative and non-redundant
# It facilitates the subsequent learning steps, and in some cases leading to better human interpretations.
# Feature extraction is a dimensionality reduction process, where 
# an initial set of raw variables is reduced to more manageable groups (features) for processing, 
# while still accurately and completely describing the original data set.

# 1 Treinar o modelo
# 2 Fazer feature extraction


class CNN_model:

	def _init_(self, input_shape, epochs):
		self.input_shape = input_shape;
		self.epochs = epochs
		loadDataset()
		createModel()

	def createModel():
		# create model
		self.model = Sequential()

		# add model layers
		# all these -> TODO: best solution? why?
		self.model.add(Conv2D(64, kernel_size=3, activation='relu', input_shape=self.input_shape)) 
		self.model.add(Conv2D(32, kernel_size=3, activation='relu'))
		self.model.add(Flatten())
		self.model.add(Dense(10, activation='softmax'))

	def loadDataset():
		# read parsing output and split into train and test sets
		(train_src, train_labels), (test_src, test_labels) = load_data()

		# Data pre-processing
		self.train_src = X_train.reshape(60000,28,28,1) # TODO: reshape according to parse dataset and model
		self.test_src  = X_test.reshape(10000,28,28,1)  # TODO: reshape according to parse dataset and model

		# one-hot encode target column 
		self.validation_data = (to_categorical(train_labels), to_categorical(test_labels)) # TODO: best solution?
		
	def train():
		self.compile()
		self.fit(self.epochs)

	def compile():
		self.model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

	def fit(epochs):
		self.model.fit(self.train_src, self.train_labels, validation_data=self.validation_data, epochs=epochs)






