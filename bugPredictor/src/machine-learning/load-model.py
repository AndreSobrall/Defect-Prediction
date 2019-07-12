# load and evaluate a saved model
from keras.models import load_model
from evaluate_model import eval_model
import bugs_dot_jar



# load dataset
(train_src, train_labels), (test_src, test_labels) = bugs_dot_jar.load_dataset("camel", 0.7)

# print("\n\nModel Metadata: ")
# print("Max sequence Length: ", len(train_src[1]))
# print("Dataset Shape: ", train_src.shape)
# print("Test set Shape: ", test_src.shape,"\n")


print(train_src.shape[0])
print(train_labels.shape[0])

print("Input")
print(train_labels[0:10])

(rand_src, rand_labels) = randomize_row_placement(train_src[0:10], train_labels[0:10])

print("Output")
print(rand_labels)

# load model
# model = load_model('model.h5')
# # model.summary()
# print("Model loaded from disk")

# eval_model(model, test_src, test_labels)

