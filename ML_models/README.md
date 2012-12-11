Implements ML models

`readMatrix.m` has been tailored for our us by simple revision
You can directly use cmd in MATLAB interactive interface
<pre>
[matrix, category] = readMatrix('dataset_compressed.txt');
matrix = full(matrix);
category = full(category);
</pre>

To fully recover the non-compressed dataset.

'dataset_compressed.txt' store the samples in compressed format same as used in 
CS229 problem set 2 Q2