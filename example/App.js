import React  from 'react';
import {Platform, StyleSheet, Text, View, TouchableHighlight, TextInput, ScrollView} from 'react-native';
import { Client, EventEmitter } from 'react-native-hecom-easemob';

const test = {appKey: 'easemob-demo#chatdemoui', account: 'zzg980820', password: '123'};

export default class extends React.Component {
    constructor(props) {
        super(props);
        Client.initWithAppKey(test.appKey);
        EventEmitter.registerEventHandler(newMsg => {
            this.setState(({message}) => ({message: message + '\n' + JSON.stringify(newMsg)}));
        });
        this.state = {message: ''}
    }

    render() {
        return (
            <View style={styles.container}>
                <TextInput
                    placeholder={'账号'}
                    onChangeText={text => this.account = text}
                    defaultValue={test.account}
                />
                <TextInput
                    placeholder={'密码'}
                    onChangeText={text => this.password = text}
                    defaultValue={test.password}
                />
                {Platform.OS === 'android' &&
                <TouchableHighlight onPress={() => EventEmitter.notifyJSDidLoad()}>
                    <Text style={styles.welcome}>JS Loaded</Text>
                </TouchableHighlight>}
                <TouchableHighlight onPress={() => {
                    if (this.account && this.password) {
                        Client.login(this.account, this.password)
                    }
                }}>
                    <Text style={styles.welcome}>
                        Login
                    </Text>
                </TouchableHighlight>
                <ScrollView>
                    <Text>{this.state.message}</Text>
                </ScrollView>
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
});
