%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Apply SVM to the data set and generate prediction.
% we will use three split criterions and choose the one with
% min cross validation error as the final predictor tree
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function predicates = runSVM(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)
    svmStruct = svmtrain(featureMatrixTrain,labelTrain,'kernel_function','rbf','method','SMO');
    % model 'quadratic' 'polynomial' 'rbf'(work)
    predicates = svmclassify(svmStruct,featureMatrixTest);
    
end
    