import React from 'react';
import { StyleSheet, Text, View, TouchableHighlight, TextInput } from 'react-native';
import { initWithAppKey, Client } from 'react-native-hecom-easemob';

const demo = {appKey: 'easemob-demo#chatdemoui', account: 'zzg980820', password: '123'};

export default class extends React.Component {
    constructor(props) {
        super(props);
        initWithAppKey(demo.appKey);
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
});
