%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Implement feature selection based on elminated features
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function [featureMatrixTrain, featureMatrixTest] = eliminateFeature(...
    feature_eliminated, featureMatrixTrain, featureMatrixTest)

    display(['Eliminat feature -- ', feature_eliminated]);
    switch(feature_eliminated)
        case {'num words','num stop words','num cap words',...
                'num puncs','num nps btw'}
            indices = 1;
        case {'e1 entity','e2 entity'}
            indices = 1 : 7;
        case {'left e1 pos', 'right e2 pos'}
            indices = 1: 36;
        otherwise
            indices = [];
    end
    featureMatrixTrain(:, indices) = [];
    featureMatrixTest(:, indices) = [];
    display(['feature matrix dimention ', ...
        num2str(size(featureMatrixTrain, 2))]);
end