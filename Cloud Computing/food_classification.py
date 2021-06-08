import tensorflow as tf
import os
import zipfile
import matplotlib.pyplot as plt
import random
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import keras_preprocessing
from keras_preprocessing import image
from keras_preprocessing.image import ImageDataGenerator
from tensorflow.keras import Sequential
from tensorflow.keras.layers import Conv2D,MaxPooling2D,Flatten,Dropout,Dense
import numpy as np
from keras_preprocessing.image import load_img, img_to_array

local_zip = "fruits.zip"
zip_ref = zipfile.ZipFile(local_zip, 'r')
zip_ref.extractall('/tmp/')
zip_ref.close()

train_dir = "/tmp/fruits-360/Training"
test_dir = "/tmp/fruits-360/Test"
kiwi_train_dir = train_dir+"/Kiwi"
kiwi_test_dir = test_dir+"/Kiwi"
lemon_train_dir = train_dir+"/Lemon"
lemon_test_dir = test_dir+"/Lemon"
orange_train_dir = train_dir+"/Orange"
orange_test_dir = test_dir+"/Orange"

kiwi_train = len(os.listdir(kiwi_train_dir))
kiwi_test = len(os.listdir(kiwi_test_dir))
lemon_train = len(os.listdir(lemon_train_dir))
lemon_test = len(os.listdir(lemon_test_dir))
orange_train = len(os.listdir(orange_train_dir))
orange_test = len(os.listdir(orange_test_dir))

file,fn = (kiwi_train,kiwi_test,lemon_train,lemon_test,orange_train,orange_test),("kiwi_train","kiwi_test","lemon_train","lemon_test","orange_train","orange_test")
x_coord = [x for x in range(len(fn))]
plt.bar(x_coord, file, tick_label=fn)
plt.xticks(rotation=90)
plt.show()

print("test size of all images:")
print('Kiwi: %.2f'%(kiwi_test/(kiwi_train+kiwi_test)), 'Lemon: %.2f'%(orange_test/(orange_train+orange_test)),'Orange: %.2f'%(lemon_test/(lemon_train+lemon_test)))

sample_kiwi = random.choice(os.listdir(kiwi_train_dir))
sample_lemon = random.choice(os.listdir(lemon_train_dir))
sample_orange = random.choice(os.listdir(orange_train_dir))
sample = [os.path.join(kiwi_train_dir,sample_kiwi)]+[os.path.join(lemon_train_dir,sample_lemon)]+[os.path.join(orange_train_dir,sample_orange)]

fig = plt.gcf()
fig.set_size_inches(16, 16)
for i, img_path in enumerate(sample):
  plt.subplot(4,4,i+1)
  plt.axis('Off')
  img = mpimg.imread(img_path)
  plt.imshow(img)
plt.show()

class myCallback(tf.keras.callbacks.Callback):
  def on_epoch_end(self, epoch, logs={}):
    if(logs.get('loss')<0.01):
      print("\nReached below 0.01% loss, cancelling training")
      self.model.stop_training = True

callbacks = myCallback()

#Define Training Data using Image Augmentation
TRAINING_DIR = train_dir
training_datagen = ImageDataGenerator(
      rescale=1./255,
      rotation_range=40,
      width_shift_range=0.2,
      height_shift_range=0.2,
      shear_range=0.2,
      zoom_range=0.2,
      brightness_range=[1.0,2],
      horizontal_flip=True,
      fill_mode='nearest')

train_generator = training_datagen.flow_from_directory(
	TRAINING_DIR,
	target_size=(100,100),
	class_mode='categorical',
  batch_size=32
)

#Define Validation Data
VALIDATION_DIR = test_dir
validation_datagen = ImageDataGenerator(rescale = 1./255)

validation_generator = validation_datagen.flow_from_directory(
	VALIDATION_DIR,
	target_size=(100,100),
	class_mode='categorical',
  batch_size=32
)

class myCallback(tf.keras.callbacks.Callback):
  def on_epoch_end(self, epoch, logs={}):
    if(logs.get('loss')<0.01):
      print("\nReached below 0.01% loss, cancelling training")
      self.model.stop_training = True

callbacks = myCallback()

#Define Model Sequential Using CNN
model = Sequential([
    Conv2D(64, (3,3), activation='relu', input_shape=(100, 100, 3)),
    MaxPooling2D(2, 2),
    Conv2D(64, (3,3), activation='relu'),
    MaxPooling2D(2,2),
    Conv2D(128, (3,3), activation='relu'),
    MaxPooling2D(2,2),
    Conv2D(128, (3,3), activation='relu'),
    MaxPooling2D(2,2),
    Flatten(),
    Dropout(0.5),
    Dense(512, activation='relu'),
    Dense(3, activation='softmax')
])
epochs=10
model.summary()
model.compile(loss = 'categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
history = model.fit(train_generator, epochs=epochs, steps_per_epoch=20, validation_data = validation_generator, validation_steps=3, callbacks=[callbacks])

acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(acc))

plt.plot(epochs, acc, 'r', label='Training accuracy')
plt.plot(epochs, val_acc, 'b', label='Validation accuracy')
plt.title('Training and validation accuracy')
plt.legend()
plt.figure()

plt.plot(epochs, loss, 'r', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()
plt.show()

def process(image):
  image = load_img(image, target_size=(100,100))
  img = img_to_array(image)
  img = np.expand_dims(img, axis=0)
  img = np.vstack([img])
  return img, image

def classify(classes):
  if classes[0][0]==1:
    return "Kiwi"
  elif classes[0][1]==1:
    return "Lemon"
  elif classes[0][2]==1:
    return "Orange"
  return "None"

def visualize(image):
  plt.imshow(image)
  plt.axis('off')
  plt.show()

def get_image_classify(image_path):
  img, image = process(image_path)
  classes = model.predict(img)

  return classify(classes)
