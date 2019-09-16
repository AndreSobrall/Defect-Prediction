from keras.models import Sequential
from keras.layers import Dense, Conv1D, MaxPooling1D, Flatten, Embedding
from evaluate_model import eval_model
from evaluate_model import randomize_row_placement
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

# Read parsing output and split into train and test sets
datasets = bugs_dot_jar.load_data(0.7)
lst = {"accumulo", "camel", "commons-math", "flink", "jackrabbit-oak", "logging-log4j2", "maven", "wicket"}

header = "dataset -> F1 Score\n"
result = [header]

for dataset in datasets:
	# Model data
	dataset_name = dataset[0]
	if(dataset_name == "accumulo"):
		continue
	(train_src, train_labels), (test_src, test_labels) = dataset[1]
	# (train_src, train_labels) = randomize_row_placement(train_src, train_labels)

	# Model Metadata
	max_sequence_length = len(train_src[1])
	nr_of_features 		= bugs_dot_jar.getNumberOfFeatures(dataset_name)

	# Parameters
	epochs = 10
	embedding_size = 32
	nr_of_filters = 32
	filter_length = 3
	
	# train model
	print("\n\nModel Metadata:")
	print("Dataset Name: ", dataset_name)
	print("Max sequence Length: ",max_sequence_length)
	print("Number of Features: ",nr_of_features)
	print("Number of Filters: ", nr_of_filters)
	print("Dataset Shape: ", train_src.shape)
	print("Test set Shape: ", test_src.shape,"\n")

	# create model
	model = Sequential()

	# add model layers
	model.add(Embedding(nr_of_features + 1,
	                    embedding_size,  # Embedding size
	                    input_length=max_sequence_length))

	model.add(Conv1D(nr_of_filters, filter_length, activation='relu'))
	model.add(MaxPooling1D(nr_of_filters))
	model.add(Flatten())
	model.add(Dense(units=64, activation='relu'))
	model.add(Dense(units=32, activation='relu'))
	model.add(Dense(units=1, activation='sigmoid'))

	# compile model
	model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])

	# train model
	model.fit(train_src, train_labels, validation_data=(test_src, test_labels), epochs=epochs)
	# print(model.summary())

	# evaluate the model
	loss, accuracy = model.evaluate(test_src, test_labels, verbose=0)

	f1_score = eval_model(model, test_src, test_labels)

	result.append(dataset_name + " " + str(f1_score) +"\n")
	
	# save model and architecture to single file
	# model.save("model-"+dataset_name+"-embedding-",embed,".h5")
	# print("Saved model to disk")

f = open("results/last_results.txt", "a")
for line in result:
	f.write(line)
f.close()
