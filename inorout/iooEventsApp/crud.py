from sqlalchemy.orm import Session, joinedload
from passlib.hash import bcrypt
from . import models, schemas
from sqlalchemy import desc


def get_community_events(db: Session, user, skip: int = 0,limit: int = 20):
    
    
    #q = db.query(models.CommunityEvent).filter(models.CommunityEvent.id==models.EventImage.event_id).order_by(models.CommunityEvent.occurence.desc()).offset(skip).limit(limit).all()
    #q = db.query(models.CommunityEvent).filter(models.CommunityEvent.id==models.EventImage.event_id).outerjoin(models.InterestedApplicant,models.InterestedApplicant.event_id==models.CommunityEvent.id).filter(models.InterestedApplicant.event==None).offset(skip).limit(limit).all()
    subq = db.query(models.InterestedApplicant.event_id).join(models.User).filter(models.User.id==user.id).subquery()
    q = db.query(models.CommunityEvent).where(models.CommunityEvent.id.notin_(subq)).filter(models.CommunityEvent.id==models.EventImage.event_id).filter(models.CommunityEvent.user_owner_id!=user.id).filter(models.CommunityEvent.status==True).offset(skip).limit(limit).all()

 
    return q 

def get_community_event(event_id, db: Session, user, skip: int = 0,limit: int = 20):
    q = db.query(models.CommunityEvent).filter(models.CommunityEvent.id==event_id).offset(skip).limit(limit).first()
    #q = db.query(models.CommunityEvent).offset(skip).limit(limit).all()
   
    return q 


def get_event_applicants(db, event_id ,skip: int = 0,limit=20):
    #q = db.query(models.CommunityEvent).options(joinedload(models.CommunityEvent.users)).filter(models.CommunityEvent.id==event_id).offset(skip).limit(limit).one()
    #q = db.query(models.User).filter(models.User.events.any(models.CommunityEvent.id==event_id)).all()
    q = db.query(models.CommunityEvent).options(joinedload(models.CommunityEvent.users)).where(models.CommunityEvent.id==event_id).one()
    #q = db.query(models.User).join(models.CommunityEvent, models.User.events).filter(models.CommunityEvent.id == event_id)
    return [applicant.user for applicant in q.users]


def get_event_applicant(applicant_id, db, event_id ,skip: int = 0,limit=20):
    q = db.query(models.User).filter(models.User.id==applicant_id).one()
    subq = db.query(models.InterestedApplicant).filter(models.InterestedApplicant.user_id==applicant_id).filter(models.InterestedApplicant.event_id==event_id).first()
    resultSet={}
    if q and subq:
        resultSet = q.to_dict()
        resultSet.update(subq.to_applicant_dict())
        return resultSet


def get_my_potential_events(user, db,skip: int = 0,limit=20):
    q = db.query(models.User).options(joinedload(models.User.events)).where(models.User.id==user.id).one()
    #q = db.query(models.User).join(models.CommunityEvent, models.User.events).filter(models.CommunityEvent.id == event_id)
    resultSet = []
    for ev in q.events:
        resultRow = {}
        resultRow["user_full_name"] = ev.event.user_full_name
        resultRow["name"] = ev.event.name
        resultRow["user_status"]=ev.user_status
        resultSet.append(resultRow)
    
    return resultSet


def get_my_community_events(db: Session, user, skip: int = 0, limit: int = 20):
    q = db.query(models.CommunityEvent).filter(models.CommunityEvent.id==models.EventImage.event_id).filter(models.CommunityEvent.user_owner_id==user.id).offset(skip).limit(limit).all()
    return q

# def get_product(db: Session, product_name: str):
#     return db.query(models.CommunityEvent).filter(models.Product.name == product_name).first()


def create_user(db: Session, user: schemas.UserCreate):
    db_user = models.User(
        email=user.email,
         hashed_password=bcrypt.hash(user.password),
         fullName = user.fullName,
         city = user.city,
         country = user.country,
         bio = user.bio,
         gender = user.gender,
         dob = user.dob,
         socialMedia = user.socialMedia,
         socialMediaHandle = user.socialMediaHandle)

    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user



def create_user_with_image(db: Session, email,fullName,city,country,gender,dob,bio,socialMedia,socialMediaHandle,url,password):
    db_user = models.User(
        email=email,
         hashed_password=bcrypt.hash(password),
         fullName = fullName,
         city = city,
         country = country,
         bio = bio,
         gender = gender,
         dob = dob,
         socialMedia = socialMedia,
         socialMediaHandle = socialMediaHandle,
         image = url)

    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user


def add_community_event(db: Session, eventAdd: schemas.CommunityEventCreate, user_owner_id: int):
    db_communityEvent = models.CommunityEvent(
        name = eventAdd.name,
        type = eventAdd.type,
        description =eventAdd.description,
        num_Of_Applicants_needed = eventAdd.num_Of_Applicants_needed,
        occurence = eventAdd.occurence,
        user_owner_id = user_owner_id
    )

    db.add(db_communityEvent)
    db.commit()
    db.refresh(db_communityEvent)
    # db_Image = models.EventImage(
    #     url = eventImage.url,
    #     event_id = db_communityEvent.id
    # )

    # db.add(db_Image)
    # db.commit()
    # db.refresh(db_Image)


def create_community_event(db: Session, name , type  , description ,num_Of_Applicants_needed, occurence ,user_owner_id , eventImage: schemas.EventImage, user_full_name: str):
    db_communityEvent = models.CommunityEvent(
        name = name,
        type = type,
        description = description,
        num_Of_Applicants_needed = num_Of_Applicants_needed,
        user_owner_id = user_owner_id,
        occurence = occurence,
        user_full_name = user_full_name
    )

    db.add(db_communityEvent)
    db.commit()
    db.refresh(db_communityEvent)
    db_Image = models.EventImage(
        url = eventImage.url,
        event_id = db_communityEvent.id
    )

    db.add(db_Image)
    db.commit()
    db.refresh(db_Image)


    return db_communityEvent



def post_applicant_acceptance(db: Session, applicant: schemas.ApplicantInterest):
    db_applicant_interest = db.query(models.InterestedApplicant).filter(models.InterestedApplicant.event_id==applicant.event_id).filter(models.InterestedApplicant.user_id==applicant.user_id).one()
    db_applicant_interest.user_status=1

    db_event = db.query(models.CommunityEvent).filter(models.CommunityEvent.id==applicant.event_id).one()
    db_event.num_Of_Applicants =models.CommunityEvent.num_Of_Applicants+1
    if db_event.num_Of_Applicants_needed == db_event.num_Of_Applicants:
        db.event.status=False
        q = db.query(models.InterestApplicant).filter(models.InterestedApplicant.user_status==0).filter(models.InterestedApplicant.event_id==applicant.event_id).all()
        for el in q:
            el.user_status = 2

        
    
    db.commit()
    db.refresh(db_applicant_interest)
    db.refresh(db_event)

    return True


def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()


def get_user_by_email(db: Session, email: str):
    return db.query(models.User).filter(models.User.email == email).first()


def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()


def create_applicant_interest(db: Session,user: schemas.User,interest: schemas.ApplicantInterestBase):
    db_applicantInterest = models.InterestedApplicant(
        event_id = interest.event_id,
        user_id = user.id,
        user_event_description = interest.user_event_description
    )

    db.add(db_applicantInterest)
    db.commit()
    db.refresh(db_applicantInterest)

    return db_applicantInterest