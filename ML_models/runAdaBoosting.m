%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Implement Adaboosting with beta 0.5
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function predicates = runAdaBoosting(featureMatrixTrain, ...
        labelTrain, featureMatrixTest)
    display('Running Adaboosting...');
    global NUM_BOOSTING_ITR;
    % firstly, define uniform distribution over all samples, equal weights
    numTrain = size(featureMatrixTrain, 1);
    distribution = ones(numTrain, 1) ./ numTrain;
    weights = zeros(1, NUM_BOOSTING_ITR);
    models = {};
    % generate samples based on the distribution
    for t = 1 : NUM_BOOSTING_ITR
        display(['Boosting Iteration: ', num2str(t)]);
        %% generate num train samples based on updated distribution
        indices = discretesample(distribution, numTrain);
        featureMatrixTrain = featureMatrixTrain(indices,:);
        labelTrain = labelTrain(indices);
        
        %% generate model . you can change the method here
        [predicates model] = runNaiveBayes(featureMatrixTrain, ...
            labelTrain, featureMatrixTrain);
        
        %% calculate alph and error
        fp_indices = intersect(find(labelTrain == 0),find(predicates == 1));
        fn_indices = intersect(find(labelTrain == 1),find(predicates == 0));
        false_indices = [fp_indices; fn_indices];
        
        error = sum(distribution(false_indices));
        
        %% update weights for current model and store current model
        alpha = 0.5 * log((1 - error) / error);
        weights(t) = alpha;
        models{t} = model;
        
        %% updated distribution for next iteration
        distribution(indices) = distribution(indices) .* ...
            exp(-alpha * sign(double(labelTrain == predicates) - 0.5));
        
        distribution = distribution ./ sum(distribution);
    end
    predicates = zeros(size(featureMatrixTest, 1), 1);
    for t = 1 : NUM_BOOSTING_ITR
        % for different models, the predict function may be
        % written in different ways, you may change accordingly
        predicates = predicates + weights(t) * ...
            models{t}.predict(featureMatrixTest);
    end
    
    predicates = (1/2) * sign(predicates - 0.5) + 0.5;
    
end