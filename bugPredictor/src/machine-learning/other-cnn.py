
from keras.models import Sequential
from keras.layers import Dense, Conv1D, MaxPooling1D, Flatten, Embedding
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

ANSI_RESET = "\u001B[0m"
ANSI_RED   = "\u001B[31m"
ANSI_GREEN = "\u001B[32m"

# Read parsing output and split into train and test sets
trainning_set_size = 0.7
(train_src, train_labels), (test_src, test_labels) = bugs_dot_jar.load_data(trainning_set_size)

# Model Metadata
max_sequence_length = len(train_src[1])
nr_of_features 		= bugs_dot_jar.getNumberOfFeatures()
nr_of_filters			= 5

print("\n" + ANSI_GREEN + "Model Metadata:" + ANSI_RESET)
print("Max sequence Length: ",max_sequence_length)
print("Number of Features: ",nr_of_features)
print("Number of Filters: ",nr_of_filters)
print("Dataset Shape: ", train_src.shape,"\n")

# create model
model = Sequential()

# # add model layers
model.add(Embedding(nr_of_features + 1,
                    64,  # Embedding size
                    input_length=max_sequence_length))
model.add(Conv1D(64, nr_of_filters, activation='relu'))
model.add(MaxPooling1D(nr_of_filters))
model.add(Flatten())
model.add(Dense(units=64, activation='relu'))
model.add(Dense(units=1, activation='sigmoid'))

# compile model
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
print(model.summary())

# train model
epochs = 2
model.fit(train_src, train_labels, validation_data=(test_src, test_labels), epochs=epochs)
