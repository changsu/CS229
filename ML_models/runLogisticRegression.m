%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Implement logistic regression algorithm
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function predicates = runDecisionTree(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)
   
   % generate lr coefficients
   coefficients = getCoefficients(featureMatrixTrain, labelTrain);
   
   % make prediction, we simply use 0.5 as seperator rather than
   % using F1 score to select seperator
   predicates = 1 ./ (1+exp(-featureMatrixTest * coefficients));
   predicates(find(predicates >= 0.5)) = 1;
   predicates(find(predicates < 0.5)) = 0;
   
end
