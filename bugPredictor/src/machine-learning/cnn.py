from keras.utils import to_categorical
from keras.models import Sequential
from keras.layers import Dense, Conv2D, Flatten
import bugs_dot_jar

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

	def __init__(self, epochs):
		self.epochs = epochs
		self.loadDataset()
		self.createModel()

	def createModel(self):
		# create model
		self.model = Sequential()

		# add model layers
		# all these -> TODO: best solution? why?
		self.model.add(Conv2D(64, kernel_size=3, activation='relu', input_shape=self.input_shape)) 
		self.model.add(Conv2D(32, kernel_size=3, activation='relu'))
		self.model.add(Flatten())
		self.model.add(Dense(10, activation='softmax'))

	def loadDataset(self):
		# read parsing output and split into train and test sets
		trainning_set_size = 0.7
		(train_src, train_labels), (test_src, test_labels) = bugs_dot_jar.load_data(trainning_set_size)

		self.train_src = train_src
		self.train_labels = train_labels
		self.test_src = test_src
		self.test_labels = test_labels

		# Data pre-processing
		# self.train_src = X_train.reshape(60000,28,28,1) # TODO: reshape according to parse dataset and model
		# self.test_src  = X_test.reshape(10000,28,28,1)  # TODO: reshape according to parse dataset and model
		self.input_shape = train_src.shape
		# one-hot encode target column 
		# self.validation_data = (to_categorical(train_labels), to_categorical(test_labels)) # TODO: best solution?
		
	def train():
		self.compile()
		self.fit(self.epochs)

	def compile():
		self.model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

	def fit(epochs):
		self.model.fit(self.train_src, self.train_labels, validation_data=self.validation_data, epochs=epochs)

	# ------------------------ # 
	#  k-fold cross validation #
	# ------------------------ # 
	#
	# The training set is split into k smaller sets
	# For each of the k “folds”:
	#	A model is trained using (k-1) folds as trainning data
	#	The resulting model is validated on the reserved k fold.
	#	i.e. it is used as a test set to compute a performance measure such as accuracy
	# The performance measure reported by k-fold cross-validation 
	# is then the average of the values computed in the loop.

	def cross_validation(k):
		scores = cross_val_score(self.model, self.train_src, self.train_labels, cv=k)
		print("Accuracy: %0.2f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))
		return scores


def main():
	cnn = CNN_model(5)
	scores = cnn.cross_validation(5)
	print(scores)

if __name__ == "__main__":
    main()




