%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% pre-process of the data
%seperate positive and negative samples, reshuffle
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function [featureMatrixTrain labelTrain featureMatrixTest labelTest] = ...
    preprocess(matrix, category)
    global RATIO;
    % seperate pos and neg samples and divide into train and test each
    pos = matrix(find(category == 1), :);
    neg = matrix(find(category == 0), :);
    neg = neg(1:size(pos,1),:);

    pos_train = pos(1:round(RATIO * size(pos,1)), :);
    pos_test = pos(size(pos_train,1) + 1:end, :);

    neg_train = neg(1:round(RATIO * size(neg,1)), :);
    neg_test = neg(size(neg_train,1) + 1:end, :);

    % combine into train and test matrix and shuffle
    trainMatrix = [pos_train ones(size(pos_train,1),1); ...
        neg_train zeros(size(neg_train,1),1)];
    testMatrix = [pos_test ones(size(pos_test,1),1); ...
        neg_test zeros(size(neg_test,1),1)];

    shuffleMatrix = [trainMatrix; testMatrix];
    shuffleMatrix = shuffleMatrix(randperm(size(shuffleMatrix,1)),:);
    trainMatrix = shuffleMatrix(1:size(trainMatrix,1),:);
    testMatrix = shuffleMatrix(1:size(testMatrix,1),:);
    
    % seperate into feature matrix and label
    featureMatrixTrain = trainMatrix(:, 1:end-1);
    labelTrain = trainMatrix(:,end);
    featureMatrixTest = testMatrix(:, 1:end-1);
    labelTest = testMatrix(:,end);
    
end