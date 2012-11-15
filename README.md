
### Git Basic Command Lines
Git comment you may find useful

<pre>
$ git pull https://github.com/changsu/CS229.git
</pre>

Update your code base to the latest version

If have confilictions after pull, solve it, modify some file....

<pre>
$ git add .
</pre>

Prepare content staged for the next commit (DO NOT forget this cmd before
you commit).

<pre>
$ git commit -m "some note for you commit"
</pre>

Store current contents in a new commit with a log message. (PLEASE put
meaningful and concise comments inside double quotes).

<pre>
$ git push https://github.com/changsu/CS229.git
</pre>
Update remote references, where you local commit will be pushed into
codebase and visible to other collaboraters.

When you succeed in pushing the code, you should see something like this
<pre>
To https://github.com/changsu/CS229.git
   e144bb6..7958840  master -> master
</pre>

Done!!

### File Structure

#### Data/
1. Seed data from the web
2. Training data corpus(should be non-finance) for generate labels

#### ML_models/
Algorirthm Implementation for ML

#### UI_tool/
Code related to UI

####  NLP/
Natural Language Processing Code:
1. Stanford Parser that transform sentence into tree structures
2. Code used for extracting features


### Database Information
Added in the future
