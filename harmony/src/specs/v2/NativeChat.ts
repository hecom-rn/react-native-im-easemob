// NativeChat.ts
import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  // ClientSpec
  init(props: { appKey?: string; options?: Object }): Promise<void>;
  login(props: {
    username: string;
    password: string;
    autoLogin: boolean;
  }): Promise<void>;
  logout(): Promise<void>;
  isLoggedIn(): Promise<boolean>;
  notifyJSDidLoad(): Promise<void>;
  registerUser(props: { username: string; password: string; }): Promise<never>;
  kickAllDevices(): Promise<never>;
  isConnected(): Promise<boolean>;
  fetchToken(props: { username: string; password: string; }): Promise<never>;

  // ChatManagerSpec
  getConversation(props: {
    conversationId: string;
    type: number;
    ifCreate: boolean;
  }): Promise<Object | undefined>;
  getAllConversations(): Promise<Array<Object> | undefined>;
  deleteConversation(props: { conversationId: string; ifClearAllMessage: boolean }): Promise<void>;
  loadMessages(props: { conversationId: string; chatType: number; fromId: string; count: number; searchDirection: number}): Promise<Array<Object>>;
  fetchHistoryMessagesFromServer(props: { conversationId: string; chatType: number; fromId: string; count: number }): Promise<Object>;
  deleteMessage(props: { conversationId: string; chatType: number; messageId: string }): Promise<void>;
  recallMessage(props: { conversationId: string; chatType: number; messageId: string }): Promise<Object>;
  sendMessage(props: { conversationId: string; chatType: number; messageType: number; to: string; body: Object; messageExt: Object }): Promise<Object>;
  insertMessage(props: { conversationId: string; chatType: number; messageType: number; from: string; to: string; timestamp: number; localTime: number; direction: number; body: Object; messageExt: Object; messageId: string; isRead: boolean }): Promise<Object>;
  markAllMessagesAsRead(props: { conversationId: string; chatType: number }): Promise<void>;
  deleteAllMessages(props: { conversationId: string; chatType: number }): Promise<void>;
  updateMessageExt(props: { messageId: string; ext?: Object }): Promise<void>;

  // GroupManagerSpec
  createGroup(props: { subject: string; description: string; invitees: Array<string>; message: string; setting: Object }): Promise<Object>;
  getJoinedGroups(): Promise<Array<Object>>;
  getGroupMemberList(props: { groupId: string; cursor: string; pageSize: number }): Promise<Array<Object>>;
  getGroupSpecification(props: { groupId: string }): Promise<Object>;
  addOccupants(props: { groupId: string; members: Array<string> }): Promise<Object>;
  removeOccupants(props: { groupId: string; members: Array<string> }): Promise<Object>;
  changeGroupSubject(props: { groupId: string; subject: string }): Promise<Object>;
  leaveGroup(props: { groupId: string }): Promise<void>;
  destroyGroup(props: { groupId: string }): Promise<void>;
  updateGroupExt(props: { groupId: string; ext: string }): Promise<Object>;
  changeGroupDescription(props: { groupId: string; description: string }): Promise<Object>;
  updateGroupOwner(props: { groupId: string; newOwner: string }): Promise<Object>;

  // APNsSpec
  getPushOptionsFromServer(): Promise<string>;
  setApnsNickname(props: { name: string }): Promise<void>;
  setIgnoreGroupPush(props: { groupId: string; groupType: number; ignore: boolean }): Promise<void>;
  getIgnoreGroupPush(props: { groupId: string; groupType: number }): Promise<void>;
  setNoDisturbStatus(props: { startH: number; startM: number; endH: number; endM: number }): Promise<void>;
}

export default TurboModuleRegistry.get<Spec>(
  'RTNChat',
) as Spec | null;
