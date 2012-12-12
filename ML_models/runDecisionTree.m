%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Apply Decision Tree to the data set and generate prediction.
% There're some plotting along the way.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function predicates = runDecisionTree(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)

    minkvLoss = Inf;
    %% run three splitCriterions;
    display('Running gdi splitter...');
    ctree = ClassificationTree.fit(featureMatrixTrain,labelTrain, ...
            'SplitCriterion', 'gdi');
    % get cross validation error
    cvLoss = kfoldLoss(crossval(ctree));        
    if (cvLoss < minkvLoss)
        minkvLoss = cvLoss;
        finalCtree = ctree;
    end
    
    display('Running twoing splitter...');
    ctree = ClassificationTree.fit(featureMatrixTrain,labelTrain, ...
        'SplitCriterion', 'twoing');
    cvLoss = kfoldLoss(crossval(ctree));
    if (cvLoss < minkvLoss)
        minkvLoss = cvLoss;
        finalCtree = ctree;
    end
    
    display('Running deviance splitter...');
    ctree = ClassificationTree.fit(featureMatrixTrain,labelTrain, ...
        'SplitCriterion', 'deviance');
    cvLoss = kfoldLoss(crossval(ctree));
    if (cvLoss < minkvLoss)
        minkvLoss = cvLoss;
        finalCtree = ctree;
    end
    
    %% display final ctree and it's cross validation error
    result = ['min cross validation loss ', num2str(minkvLoss)];
    display(result);
    finalCtree
    
    %% predict on the test data
    predicates = predict(finalCtree, featureMatrixTest);
    
end