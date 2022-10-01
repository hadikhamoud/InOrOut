from psycopg2 import Date
from sqlalchemy import Boolean, Column, DateTime, Float, ForeignKey, Integer, String, Date, UniqueConstraint
from sqlalchemy.orm import relationship
from passlib.hash import bcrypt
from sqlalchemy.sql import func
from sqlalchemy.ext.associationproxy import association_proxy



from .database import Base

class CommunityEvent(Base):
    __tablename__ = "Community_Event"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String)
    type = Column(String)
    description = Column(String)
    occurence = Column(DateTime)
    num_Of_Applicants = Column(Integer,default=0)
    user_owner_id = Column(Integer,default = 0)
    user_full_name = Column(String)
    num_Of_Applicants_needed = Column(Integer,default=0)
    status = Column(Boolean, default = True)
    post_date = Column(DateTime,server_default=func.now())
    images = relationship("EventImage")
    users = relationship("InterestedApplicant", back_populates='event')

    def to_dict(self):
        keys = self.__mapper__.attrs.keys()
        attrs = vars(self)
        return { k : attrs[k]  for k in keys}




class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, unique=True, index=True)
    fullName = Column(String)
    city = Column(String)
    country = Column(String)
    gender = Column(Integer)
    dob = Column(Date)
    bio = Column(String)
    hashed_password = Column(String)
    socialMedia = Column(Integer)
    socialMediaHandle = Column(String)
    image = Column(String)
    is_active = Column(Boolean, default=True)
    events = relationship("InterestedApplicant", back_populates='user')





    @classmethod
    async def get_user(cls,email):
        return cls.get(email=email)
    
    def to_dict(self):
        keys = self.__mapper__.attrs.keys()
        attrs = vars(self)
        result = {}
        for k in keys:
            if k!="events":
                result[k] = attrs[k]
        return result

    def to_encode_dict(self):
        return {"id": self.id, "fullName" : self.fullName}

    def verify_password(self,password):
        return bcrypt.verify(password,self.hashed_password)




class EventImage(Base):
    __tablename__ = "Event_Image"
    id = Column(Integer, primary_key=True, index=True)
    event_id = Column(Integer,ForeignKey("Community_Event.id"))
    url = Column(String)

    def to_dict(self):
        keys = self.__mapper__.attrs.keys()
        attrs = vars(self)
        return { k : attrs[k]  for k in keys}





class InterestedApplicant(Base):
    __tablename__ = "Interested_Applicants"
    user_id = Column(ForeignKey("users.id"),primary_key = True)
    event_id = Column(ForeignKey("Community_Event.id"),primary_key = True)
    user_status = Column(Integer,default = 0)
    user_event_description = Column(String)

    event = relationship("CommunityEvent", back_populates="users")
    user = relationship("User", back_populates="events")
    
    user_fullName = association_proxy(target_collection="user",attr="fullName")
    event_name= association_proxy(target_collection="event",attr="name")

    __table_args__ = (UniqueConstraint('user_id', 'event_id', name='_user_customer_comb'),
                     )

    def to_applicant_dict(self):
        return  {"user_status": self.user_status,
        "user_event_description": self.user_event_description
        }
                    

