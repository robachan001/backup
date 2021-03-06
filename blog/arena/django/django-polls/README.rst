=====
Polls
=====

Polls is a simple Django app to conduct Web-based polls. For each
question, visitors can choose between a fixed number of answers.

Detailed documentation is in the "docs" directory.


How to build
------------
1. python setup.py sdist
2. pip install --user dist\django-polls-0.1.zip
3. pip unstall django-polls (to uninstall)
4. pip list (to see which has been installed)

Quick start
-----------

1. Add "polls" to your INSTALLED_APPS setting like this::
INSTALLED_APPS = (
...
	’polls’,
)

2. Include the polls URLconf in your project urls.py like this::

	url(r’^polls/’, include(’polls.urls’)),

3. Run ‘python manage.py syncdb‘ to create the polls models.

4. Start the development server and visit http://127.0.0.1:8000/admin/
   to create a poll (you’ll need the Admin app enabled).

 5. Visit http://127.0.0.1:8000/polls/ to participate in the poll.