from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import cloudinary
from dotenv import load_dotenv
load_dotenv()

# Use the variable with:
import os





cloudinary.config(
    cloud_name= "inoroutapp",
    api_key= os.environ.get("API_KEY"),
    api_secret =os.environ.get("API_SECRET")
)

SQLALCHEMY_DATABASE_URL = os.environ.get("SQLALCHEMY_DATABASE_URL")

engine = create_engine(SQLALCHEMY_DATABASE_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()
