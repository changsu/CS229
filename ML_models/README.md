Implements ML models

### How to Run
<pre>
run main
</pre>

### How to change paramteters
We put the control inside code rather than exporting as arguments passed in

Here you can change the models
<pre>
methods = {'naive bayes' 'svm' 'decision tree' ...
    'logistic regression' 'boosting'};
method = methods{4};
</pre>

Here you can enable feature selection mode
<pre>
enableFeatureSelection = false;
</pre>

### Functionalities

#### Main Functions
`main.m` - main function that reads in, pre-preprocesses data, applies different models and trigger result evaluation.

`runSVM.m` - run SVM classifier

`runNaiveBayes.m` - run Naive Bayes classifier

`runDecisionTree.m` - run Decision Tree classifier

`runAdaBoosting.m` - run adaboosting on Naive Bayes week classifier

`runLogisticRegress.m` - run logistic regression

`getCoefficients.m` - actual implementation of logistic regression gradient descend and return coefficient

`evaluate.m` - evaluate results in metrics of precision, recall, accuracy and F1 score

`eliminateFeature.m` - run feature selection based on certain classifier

#### Utility Functions
`preprocess.m` - preprocess of the data, generate unbiased training and dataset

`readMatrix.m` - read in compressed feature vectors and labels, compose into full matrix

`discretesample.m` - help function of sampling based on certain distribution, used in Adaboosting
