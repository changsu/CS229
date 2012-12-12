%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Evaluate result returned by any model
% calculate precision, recall, F1 score
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function [precision recall accuracy F1] = evaluate(predicates, labelTest)
    display('Evaluating results...');
        
    %% prepare data
    tp = size(intersect(find(labelTest == 1),find(predicates == 1)),1);
    fp = size(intersect(find(labelTest == 0),find(predicates == 1)),1);
    fn = size(intersect(find(labelTest == 1),find(predicates == 0)),1);
    tn = size(intersect(find(labelTest == 0),find(predicates == 0)),1);
        
    %% compute precision
    precision = tp / (tp + fp);

    %% compute recall
    recall = tp / (tp + fn);
    
    %% compute accuracy
    accuracy = (tp + tn) / (tp + tn + fp + fn);
    
    %% compute F1 score
    F1 = 2 * precision * recall / (precision + recall);    
    
    %% log results
    display(['precision: ', num2str(precision)]);
    display(['recall: ', num2str(recall)]);
    display(['accuracy: ', num2str(accuracy)]);
    display(['F1 score: ', num2str(F1)]);
end