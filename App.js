import React, {useState, useEffect} from 'react';
import {View, Text, TouchableOpacity, ScrollView} from 'react-native';
import RecordScreen from 'react-native-record-screen';

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
  const onStartRecording = () => {
    RecordScreen.startRecording().catch((error) => console.error(error));
  };

  const onStopRecording = async () => {
    console.log('inside stop.....');
    alert('Inside stop...');
    const res = await RecordScreen.stopRecording().catch((error) => {
      console.warn(error);
      alert(JSON.stringify(error));
    });
    alert(JSON.stringify(res));
    if (res) {
      alert('recording res: ');
      const url = res.result.outputURL;
    }
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
    </View>
  );
};

export default App;
