from datetime import datetime, date
from fastapi import Depends, FastAPI, HTTPException, status, File, UploadFile,Form
from sqladmin import Admin
from sqlalchemy.orm import Session
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from . import crud, models, schemas, admin
from .database import SessionLocal, engine
from passlib.hash import bcrypt
import jwt
import  cloudinary
import cloudinary.uploader
import shutil



models.Base.metadata.create_all(bind=engine)





JWT_SECRET = "f9eaafba-2e49-11ea-8880-5ce0c5aee679"

app = FastAPI()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()




oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

#function that authorizes access token and provides access to user

async def get_current_active_user(db: Session = Depends(get_db), token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token,JWT_SECRET,algorithms=['HS256'])
        user = crud.get_user(db, payload.get('id'))
    except:
            raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return user

#get all the community events present 

@app.get("/communityevents/",response_model=list[schemas.CommunityEventWithImages])
async def read_events(skip: int = 0, limit: int = 20, db: Session = Depends(get_db), user: str = Depends(get_current_active_user)):
    community_events = crud.get_community_events(db, skip=skip, user = user,limit=limit)

    return community_events

#get event details

@app.get("/communityevents/this",response_model=schemas.CommunityEventWithImages)
async def read_events(event_id: int, skip: int = 0, limit: int = 20, db: Session = Depends(get_db), user: str = Depends(get_current_active_user)):
    community_event = crud.get_community_event(event_id,db, skip=skip, user = user,limit=limit)

    return community_event


#get the events that I have created (User)
@app.get("/communityevents/me",response_model=list[schemas.CommunityEventWithImages])
async def read_events(skip: int = 0, limit: int = 20, db: Session = Depends(get_db), user: str = Depends(get_current_active_user)):
    my_community_events = crud.get_my_community_events(db, skip=skip, user = user,limit=limit)
    
    return my_community_events



#get all the applicants for the event
@app.get("/applicants",response_model=list[schemas.UserApplicantOut])
async def read_event_applicants(event_id: int, skip: int = 0, limit: int = 20, db: Session = Depends(get_db), user: str = Depends(get_current_active_user)):
    my_applicants = crud.get_event_applicants(db, skip=skip, event_id = event_id,limit=limit)
    
    return my_applicants


#get the applicant for the event
@app.get("/applicants/this",response_model=schemas.UserApplicantWithDetailsOut)
async def read_event_applicant(applicant_id: int, event_id: int, skip: int = 0, limit: int = 20, db: Session = Depends(get_db), user: str = Depends(get_current_active_user)):
    my_applicant = crud.get_event_applicant(applicant_id=applicant_id, db=db, skip=skip, event_id = event_id,limit=limit)
    
    return my_applicant


@app.get("/potentialevents",response_model=list[schemas.CommunityEventWithStatus])
async def get_potential_events(skip: int = 0, limit: int = 20, db: Session = Depends(get_db), user: str = Depends(get_current_active_user)):
    my_potential_events = crud.get_my_potential_events(user,db, skip=skip,limit=limit)
    
    return my_potential_events


@app.post("/uploadimage", response_model=bool)
def uploadImage(image: UploadFile = File(...),db: Session = Depends(get_db),token: str = Depends(get_current_active_user)):

    # result = cloudinary.uploader.upload(file.file)
    # url = result.get("url")
    result = cloudinary.uploader.upload(image.file)
    url = result.get("url")
    print(url)

    return True



@app.post("/sendacceptance")
def post_acceptance(applicant: schemas.ApplicantInterest,db: Session = Depends(get_db),user: schemas.User = Depends(get_current_active_user)):
    applicantInterest = crud.post_applicant_acceptance(db=db,applicant=applicant)
    return applicantInterest

    #eventImage = schemas.EventImageCreate(url = url)


@app.post("/applicantinterest", response_model=schemas.ApplicantInterestBase)
def create_applicant_interest(interest: schemas.ApplicantInterestBase, user: schemas.User = Depends(get_current_active_user), db: Session = Depends(get_db)):
    applicantInterest = crud.create_applicant_interest(db=db,user=user,interest=interest)
    return applicantInterest


@app.post("/users", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_user(db=db, user=user)


@app.post("/usersImage", response_model=schemas.User)
def create_user(email:str = Form(),fullName: str = Form(),city: str = Form(),country: str = Form(),gender: int = Form(),dob: date = Form(), bio: str = Form(), socialMedia: int = Form(),socialMediaHandle: str = Form(),password: str= Form(),image: UploadFile = File(...), db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    result = cloudinary.uploader.upload(image.file)
    url = result.get("url")
    return crud.create_user_with_image(db = db,email = email,fullName = fullName,city = city,country = country,gender = gender,dob = dob,bio = bio,socialMedia = socialMedia,socialMediaHandle = socialMediaHandle,url = url,password = password)


@app.post("/eventcreate", response_model=schemas.CommunityEvent)
def create_event(name: str = Form(),type: str = Form(),description: str = Form(),occurence: datetime = Form(),num_Of_Applicants_needed: int = Form(),image: UploadFile = File(...),db: Session = Depends(get_db),user_owner: schemas.User = Depends(get_current_active_user)):

    result = cloudinary.uploader.upload(image.file)
    url = result.get("url")

    eventImage = schemas.EventImageCreate(url = url)

    return crud.create_community_event(db=db,name = name, type = type, description = description,num_Of_Applicants_needed = num_Of_Applicants_needed,eventImage = eventImage,occurence = occurence,user_owner_id = user_owner.id,user_full_name=user_owner.fullName)


@app.post("/eventadd",response_model=schemas.User)
def add_event(eventAdd: schemas.CommunityEventCreate, db: Session = Depends(get_db), user: schemas.User = Depends(get_current_active_user)):
    user_onwer_id = user.id
    eventAdd = crud.add_community_event(eventAdd = eventAdd,user_owner_id=user_onwer_id,db=db)
    return user





async def authenticate_user(username: str, password: str, db: Session = Depends(get_db)):
    user = crud.get_user_by_email(db,username)
    if not user:
      return False
    if not user.verify_password(password):
        return False    
    return user
    


@app.post("/token")
async def generate_token(form_data: OAuth2PasswordRequestForm = Depends(),db: Session = Depends(get_db)):

    user = await authenticate_user(form_data.username,form_data.password,db)

    if not user:
        raise HTTPException(status_code=400, detail="Incorrect username or password")

    token = jwt.encode(user.to_encode_dict(),JWT_SECRET)

    return {"access_token": token, "token_type": "bearer"}



@app.get("/users/me",response_model=schemas.User)
async def get_user(current_user: schemas.User = Depends(get_current_active_user)):
    return current_user

AppAdmin = Admin(app, engine)
AppAdmin.register_model(admin.CommunityEventAdmin)
AppAdmin.register_model(admin.EventImageAdmin)
AppAdmin.register_model(admin.UserAdmin)
