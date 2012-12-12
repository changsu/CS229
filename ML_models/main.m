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

%% declear global variables and constants
global RATIO NUM_BOOSTING_ITR;
RATIO = 0.7; % percentage of data used for training
NUM_FEATURES = size(featureMatrixTrain, 2);
NUM_TRAIN_DATA = size(featureMatrixTrain, 1);
NUM_TEST_DATA = size(featureMatrixTest, 1);
NUM_BOOSTING_ITR = 5;

%% apply differenct classifiers on the data
% decide which method to use 'decision tree'
methods = {'naive bayes' 'svm' 'decision tree' ...
    'logistic regression' 'boosting'};
method = methods{5};
% control whether to do feature selection
% enableFeatureSelection = true;
enableFeatureSelection = false;

% prepare data for feature selection if enabled, otherwise do nothing
if enableFeatureSelection
    display('Enter feature selection stage...');
    features = {'' 'num words' 'num stop words' 'num cap words' 'num puncs' ...
        'num nps btw' 'e1 entity' 'e2 entity' 'left e1 pos' 'right e2 pos'};
    % store backward feature selection result with row as feature eliminated
    % and column as precision recall accuracy and F1 score
    selectionResult = zeros(size(features,2), 4);
    % cache the original feature train and test matrix
    endIndex = size(features, 2);
else
    display('Enter normal stage using all features...');
    endIndex = 1;
end

for f = 1 : endIndex
    
    % if feature selection is enabled, eliminate one feature in
    % each iteration and return new train and test feature matrix
    if enableFeatureSelection
        [featureMatrixTrain, featureMatrixTest] = eliminateFeature(...
            features{f}, featureMatrixTrain, featureMatrixTest);
    end
    
    
    if (strcmp(method, 'decision tree'))
        [predicates,~] = runDecisionTree(featureMatrixTrain, ...
            labelTrain, featureMatrixTest);
        display('Finish decision tree building!');
        [precision recall accuracy F1] = evaluate(predicates, labelTest);
    elseif (strcmp(method, 'logistic regression'))
        predicates = runLogisticRegression(featureMatrixTrain, ...
            labelTrain, featureMatrixTest);
        display('Finish logistic regression!');
        [precision recall accuracy F1] = evaluate(predicates, labelTest);
    elseif (strcmp(method, 'naive bayes'))
        [predicates,~] = runNaiveBayes(featureMatrixTrain, ...
            labelTrain, featureMatrixTest);
        display('Finish naive bayes!');
        [precision recall accuracy F1] = evaluate(predicates, labelTest);
    elseif (strcmp(method, 'svm'))
        predicates = runSVM(featureMatrixTrain, ...
            labelTrain, featureMatrixTest);
        display('Finish svm!');
        [precision recall accuracy F1] = evaluate(predicates, labelTest);
    elseif (strcmp(method, 'boosting'))
        predicates = runAdaBoosting(featureMatrixTrain, ...
            labelTrain, featureMatrixTest);
        display('Finish boosting!');
        [precision recall accuracy F1] = evaluate(predicates, labelTest);
    end
    
    % store statistics in this round if feature selection is enabled
    if enableFeatureSelection
        selectionResult(f, :) = [precision recall accuracy F1];
    end
end

%% plot results


