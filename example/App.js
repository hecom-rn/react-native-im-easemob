/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    View, TouchableHighlight,
    TextInput,
} from 'react-native';
import { initWithAppKey, Client } from 'react-native-hecom-easemob';

const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
    android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});
const appKey = 'tusou001#v40test';
export default class App extends Component {
    constructor(props) {
        super(props);
        initWithAppKey(appKey);
    }

    render() {
        return (
            <View style={styles.container}>
                <TextInput
                    placeholder={'账号'}
                    onChangeText={text => this.account = text}
                />
                <TextInput placeholder={'密码'} onChangeText={text => this.password = text} />
                <TouchableHighlight onPress={() => {
                    if (this.account && this.password) {
                        Client.login(this.account, this.password)
                    }
                }}>
                    <Text style={styles.welcome}>
                        Login
                    </Text>
                </TouchableHighlight>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});
