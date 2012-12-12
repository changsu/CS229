%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Implement Adaboosting based on previous four classifiers
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function predicates = runAdaBoosting(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)
    
    global NUM_BOOSTING_ITR;
    % firstly, define uniform distribution over all samples, equal weights
    numTrain = size(featureMatrixTrain, 1);
    distribution = ones(numTrain, 1) ./ numTrain;
    weights = zeros(1, NUM_BOOSTING_ITR);
    Ctrees = {};
    % generate samples based on the distribution
    for t = 1 : NUM_BOOSTING_ITR
        display(['Boosting Iteration: ', num2str(t)]);
        %% generate num train samples based on updated distribution
        indices = discretesample(distribution, numTrain);
        featureMatrixTrain = featureMatrixTrain(indices,:);
        labelTrain = labelTrain(indices);
        
        %% generate decision tree
        [predicates finalCtree] = runDecisionTree(featureMatrixTrain, ...
            labelTrain, featureMatrixTrain);
        
        %% calculate alph and error
        fp_indices = intersect(find(labelTrain == 0),find(predicates == 1));
        fn_indices = intersect(find(labelTrain == 1),find(predicates == 0));
        false_indices = [fp_indices; fn_indices];
        
        error = sum(distribution(false_indices));
        
        %% update weights for current tree and store current tree
        alpha = 0.5 * log((1 - error) / error);
        weights(t) = alpha;
        Ctrees{t} = finalCtree;
        
        %% updated distribution for next iteration
        distribution(indices) = distribution(indices) .* ...
            exp(-alpha * sign(labelTrain .* predicates));
        
        distribution = distribution ./ sum(distribution);
    end
    
    for t = 1 : NUM_BOOSTING_ITR
        predicates = predicates + weights(t) * ...
            predict(Ctrees{t}, featureMatrixTest);
    end
    
    predicates = (1/2) * sign(predicates) + 0.5;
    
end