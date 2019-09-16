from sklearn.metrics import f1_score
from random import randrange
import numpy as np

# evaluate the model
def eval_model(model, test_src, test_labels):
	test_predict = model.predict(test_src)
	pred_labels = []
	
	for pred in test_predict:
		if(pred[0] > 0.5):
			pred_labels.append(1)
		else:
			pred_labels.append(0)

	# print(pred_labels)
	# print(test_labels)
	# print("F1 Score: ", f1_score(test_labels, pred_labels, average='macro'))
	return f1_score(test_labels, pred_labels, average='macro')


def randomize_row_placement(t_src, t_labels):
	nr_rows = t_labels.shape[0]
	print(nr_rows)
	rand_t_src	  = np.copy(t_src)
	rand_t_labels = np.copy(t_labels)
	# explore half of the rows
	for idx in range(int(nr_rows/2)):
		# generate random int between 0 and nr_rows
		switch_idx = randrange(nr_rows-1)
		
		if(switch_idx == idx):
			continue

		# store content to be switched
		src_content    = rand_t_src[switch_idx]
		labels_content = rand_t_labels[switch_idx]

		# update content at idx to content from switch_idx
		rand_t_src[switch_idx]    = rand_t_src[idx]
		rand_t_labels[switch_idx] = rand_t_labels[idx]

		# and vice-versa
		rand_t_src[idx]    = src_content
		rand_t_labels[idx] = labels_content

	return (rand_t_src, rand_t_labels)
