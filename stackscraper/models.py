# https://docs.djangoproject.com/en/3.2/ref/models/fields/
# Django Field types 

# from django.db import models
from djongo import models


# Table containing information about a tech stack
class TechStack(models.Model):
    APPLICATIONDATA = 'AD'
    UTILITY = 'UT'
    DEVOPS = 'DV'
    BUISINESSTOOLS = 'BT'
    TECH_STACK_TYPE = [
        (APPLICATIONDATA, 'ApplicationData'), 
        (UTILITY, 'Utility'), 
        (DEVOPS, 'DevOps'), 
        (BUISINESSTOOLS, 'BuisinessTools'),
    ]
    name = models.CharField(max_length=50)
    stack_type = models.CharField(
        max_length=2,
        choices=TECH_STACK_TYPE,
        default=APPLICATIONDATA,
    )
    description = models.TextField(blank=True)

    # Returns : 
    #    True -> APPLICATIONDATA or DevOps
    #    False -> otherwise
    def is_devtools(self):
        return self.stack_type in {self.APPLICATIONDATA, self.DEVOPS}

    # Returns : 
    #    True -> UTILITY or BUISINESSTOOLS
    #    False -> otherwise
    def is_utiltools(self):
        return self.stack_type in {self.UTILITY, self.BUISINESSTOOLS}

    class Meta:
        abstract = True


# Table containing information about a company
class Company(models.Model):
    company = models.CharField(max_length=100)
    link = models.URLField(max_length=200)
    updated = models.DateField(auto_now=True)

    class Meta:
        abstract = True

class Entry(models.Model):
    stack = models.EmbeddedField(
        model_container = TechStack,
    )

    company = models.EmbeddedField(
        model_container = Company,
    )

    headline = models.CharField(max_length=255)
# Create your models here.
