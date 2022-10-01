from sqladmin import Admin, ModelAdmin
from . import crud, models, schemas, admin




class CommunityEventAdmin(ModelAdmin, model=models.CommunityEvent):
    column_list = [models.CommunityEvent.id, models.CommunityEvent.name, models.CommunityEvent.type, models.CommunityEvent.description]



class EventImageAdmin(ModelAdmin, model=models.EventImage):
    column_list = [models.EventImage.id, models.EventImage.event_id, models.EventImage.url]



class UserAdmin(ModelAdmin, model=models.User):
    column_list = [models.User.id, models.User.fullName, models.User.email]

