import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { detectObject } from '../../src/index'


export default function App() {
  const [result, setResult] = React.useState<number | undefined>();


  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={async () => {
        const b = await detectObject('https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz14SLHr_m17QbpHJ7FzyT-4hs871RlqVu9g&usqp=CAU')
        console.log('111111111', b)
      }}>
        <Text>Start</Text>
      </TouchableOpacity>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
