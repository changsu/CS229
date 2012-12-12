%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Apply Naive Bayes to the data set and generate prediction.
% we will use three split criterions and choose the one with
% min cross validation error as the final predictor tree
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function [predicates model]= runNaiveBayes(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)
    display('Runing naive bayes...');
    model = NaiveBayes.fit(featureMatrixTrain,labelTrain,'dist','kernel');
    %for distribution we have 'normal' 'kernel' 'mvmn' 'mn'
    predicates = model.predict(featureMatrixTest);

    
end
    