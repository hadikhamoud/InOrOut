from datetime import date, datetime



from pydantic import BaseModel
from pydantic.schema import Optional




#classes for event images containing url, and linking to event, 
#event can support multiple images
class EventImageBase(BaseModel):
    url: str


class EventImageCreate(EventImageBase):
    pass

class EventImage(EventImageBase):
    id: int
    event_id: int

    class Config:
        orm_mode = True

#------------------------------------------

#community event base and extension classes

class CommunityEventBase(BaseModel):

    name:str
    type:str
    description:str
    occurence: Optional[datetime]
    num_Of_Applicants_needed: int
    



class CommunityEventCreate(CommunityEventBase):
    pass

class CommunityEvent(CommunityEventBase):
    id: int
    num_Of_Applicants: Optional[int]
    status: bool
    post_date: Optional[datetime]
    user_owner_id: Optional[int]
    user_full_name: Optional[str]
 
    class Config:
        orm_mode=True






#one to many relaionship between community events and images

class CommunityEventWithImages(CommunityEvent):
    images: list[EventImage]

    class Config:
        json_encoders={
            EventImage: lambda image: image.url
        }


#_____________________________________
#user classes 
    
class UserBase(BaseModel):
    email: str
    fullName: str
    city: str
    country: str
    gender: int
    dob: date
    bio: str
    socialMedia: int
    socialMediaHandle: str





class UserCreate(UserBase):

    password: str

class User(UserBase):
    id: int
    is_active: bool
    image: Optional[str]

    class Config:
        orm_mode = True

class UserApplicantOut(BaseModel):
    fullName: str
    city: str
    gender: int
    dob: date
    id: int

    class Config:
        orm_mode = True

class UserApplicantWithDetailsOut(UserBase):
    id: int
    user_event_description: str
    user_status: int
    image: Optional[str]

class CommunityEventWithStatus(BaseModel):
    name: str
    user_full_name: Optional[str]
    user_status: int

class UserWithInterests(User):
    events: list[CommunityEvent]



class CommunityEventWithApplicants(CommunityEvent):
    users: list[UserApplicantOut]



class ApplicantInterestBase(BaseModel):
    event_id: int
    user_event_description: Optional[str]

    class Config:
        orm_mode = True


class ApplicantInterest(ApplicantInterestBase):
    user_id: int
    user_status: Optional[int]




