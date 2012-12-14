%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% script outputs predicted label file after 
% run through all files in corpus_test folder, predict then output predicated
% label to files in corpus_label folder
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

in_prefix = {'corpus_test/output'}; 
out_prefix = {'corpus_label/label'};
suffix = {'.txt'};
filenames = {'0_5' '5_10' '10_15' '15_20' '20_25' '25_30' ...
             '30_35' '35_40' '40_45' '45_50' '50_53' '53_55' ...
             '55_60' '60_65' '65_70' '70_75' '75_80' '80_85'};
         
in_filenames = strcat(in_prefix, filenames, suffix);
out_filenames = strcat(out_prefix, filenames, suffix);

for i = 1 : size(filenames, 2)
    [matrix, category] = readMatrix(in_filenames{i});
    matrix = full(matrix);
    matrix = matrix(:,21879:end);
    predicted_labels = predict(model, matrix);
    save(out_filenames{i}, 'predicted_labels', '-ASCII')
end


