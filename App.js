import React, {useState, useEffect} from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  NativeModules,
  PermissionsAndroid,
} from 'react-native';
// import RecordScreen from 'react-native-record-screen';
const {ScreenCapture} = NativeModules;

// // recording start
// RecordScreen.startRecording().catch((error) => console.error(error));

// // recording stop
// const res = await RecordScreen.stopRecording().catch((error) =>
//   console.warn(error),
// );
// if (res) {
//   const url = res.result.outputURL;
// }

const App = () => {
  const requestExternalStoragePermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
        {
          title: 'ScreenCapture App Write External Storage Permission',
          message:
            'ScreenCapture App needs access to your external storage ' +
            'so you can record screen.',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('You can use external storage');
        requestRecordAudioPermission();
      } else {
        console.log('External storage permission denied');
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const requestRecordAudioPermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        {
          title: 'ScreenCapture App Record Audio Permission',
          message:
            'ScreenCapture App needs access to record audio ' +
            'so you can record audio.',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('You can record the audio');
        ScreenCapture.startScreenRecording('OK');
      } else {
        console.log('Record audio permission denied');
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const onStartRecording = () => {
    // RecordScreen.startRecording().catch((error) => console.error(error));
    requestExternalStoragePermission();
  };

  const onStopRecording = async () => {
    console.log('inside stop.....');
    // alert('Inside stop...');
    // const res = await RecordScreen.stopRecording().catch((error) => {
    //   console.warn(error);
    //   alert(JSON.stringify(error));
    // });
    // alert(JSON.stringify(res));
    // if (res) {
    //   alert('recording res: ');
    //   const url = res.result.outputURL;
    // }
    ScreenCapture.stopScreenRecording((path) => {
      alert(path);
    });
  };

  const onTestNativeMethod = () => {
    console.log('Inside on test native method....');
    ScreenCapture.testNativeMethod('Raushan', 'Shiekhpura');
  };

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <TouchableOpacity
        onPress={() => {
          onStartRecording();
        }}
        style={{
          paddingHorizontal: 20,
          paddingVertical: 10,
          borderRadius: 5,
          backgroundColor: 'green',
        }}>
        <Text style={{fontSize: 14, color: 'white'}}>{'Start Recording'}</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => {
          onStopRecording();
        }}
        style={{
          marginTop: 10,
          paddingHorizontal: 20,
          paddingVertical: 10,
          borderRadius: 5,
          backgroundColor: 'green',
        }}>
        <Text style={{fontSize: 14, color: 'white'}}>{'Stop Recording'}</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => {
          onTestNativeMethod();
        }}
        style={{
          marginTop: 10,
          paddingHorizontal: 20,
          paddingVertical: 10,
          borderRadius: 5,
          backgroundColor: 'green',
        }}>
        <Text style={{fontSize: 14, color: 'white'}}>{'Native Button'}</Text>
      </TouchableOpacity>
    </View>
  );
};

export default App;
