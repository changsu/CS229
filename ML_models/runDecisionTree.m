%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Apply Decision Tree to the data set and generate prediction.
% we will use three split criterions and choose the one with
% min cross validation error as the final predictor tree
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function [predicates finalCtree] = runDecisionTree(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)
    display('Running decision tree...');
    SplitCriterion = {'gdi' 'twoing' 'deviance'};
    minkvLoss = Inf;
    %% run three splitCriterions;
    for i = 1 : 3
        display(['>>Running ', SplitCriterion{i} ,' splitter...']);
        ctree = ClassificationTree.fit(featureMatrixTrain,labelTrain, ...
            'SplitCriterion', SplitCriterion{i});
        % get cross validation error
        cvLoss = kfoldLoss(crossval(ctree));
        if (cvLoss < minkvLoss)
            minkvLoss = cvLoss;
            finalCtree = ctree;
        end
    end
    
    %% display final ctree and it's cross validation error
    display(['min cross validation loss ', num2str(minkvLoss)]);
    
    %% predict on the test data
    predicates = predict(finalCtree, featureMatrixTest);
    
end