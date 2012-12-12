%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This is tha main function that controls the overall flow:
% -> load files
% -> pre-compose samples
% -> execute algorithms
% -> evaluation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clear;

%% load files
[matrix, category] = readMatrix('dataset_compressed.txt');
matrix = full(matrix);
% eliminate words feature, too big to compute in limit time
matrix = matrix(:,21879:end); 
category = full(category);

[featureMatrixTrain labelTrain featureMatrixTest labelTest] = ...
    preprocess(matrix, category);

%% declear gloabal variables and constants
NUM_FEATURES = size(featureMatrixTrain, 2);
NUM_TRAIN_DATA = size(featureMatrixTrain, 1);
NUM_TEST_DATA = size(featureMatrixTest, 1);

%% apply differenct classifiers on the data
% decide which method to use 'decision tree'
method = 'decision tree'; 

% TODO: used for feature selection
feature_eliminated = '';
    
if (strcmp(method, 'decision tree'))
    display('Running decision tree...');
    predicates = runDecisionTree(featureMatrixTrain, ...
        labelTrain, featureMatrixTest);
    display('Finishing decision tree building!');
elseif (strcmp(method, 'logistic regression'))
    % TODO
    display('Running logistic regression...');
elseif (strcmp(method, 'naive bayes'))
    % TODO
    display('Runing naive bayes...');
elseif (strcmp(method, 'svm'))
    % TODO
    display('Running svm...');
elseif (strcmp(method, 'boosting'))
    % TODO
    display('Running boosting...');
end


%% result evaluation
evaluate(predicates, labelTest);










