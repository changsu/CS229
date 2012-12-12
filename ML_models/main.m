%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This is tha main function that controls the overall flow:
% -> load files
% -> pre-compose samples
% -> execute algorithms
% -> evaluation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear;
clc;

%% load files
[matrix, category] = readMatrix('dataset_compressed.txt');
matrix = full(matrix);
% eliminate words feature, too big to compute in limit time
matrix = matrix(:,21879:end); 
category = full(category);

%% pre-process of data, seperate positive and negative samples, reshuffle
% seperate pos and neg samples and divide into train and test each
pos = matrix(find(category == 1), :);
neg = matrix(find(category == 0), :);
neg = neg(1:size(pos,1),:);

pos_train = pos(1:round(0.7 * size(pos,1)), :);
pos_test = pos(size(pos_train,1) + 1:end, :);

neg_train = neg(1:round(0.7 * size(neg,1)), :);
neg_test = neg(size(neg_train,1) + 1:end, :);

% combine into train and test matrix and shuffle
trainMatrix = [pos_train ones(size(pos_train,1),1); ...
    neg_train zeros(size(neg_train,1),1)];
testMatrix = [pos_test ones(size(pos_test,1),1); ...
    neg_test zeros(size(neg_test,1),1)];

trainMatrix = trainMatrix(randperm(size(trainMatrix,1)),:);
testMatrix = testMatrix(randperm(size(testMatrix,1)),:);

% seperate into feature matrix and label 
featureMatrixTrain = trainMatrix(:, 1:end-1);
labelTrain = trainMatrix(:,end);
featureMatrixTest = testMatrix(:, 1:end-1);
labelTest = testMatrix(:,end);

% clear non-use variables, save memory space
clear pos neg pos_train pos_test neg_train neg_test trainMatrix ...
    testMatrix matrix category;

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











