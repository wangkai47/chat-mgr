<template>
  <div class="smart-dialog">
    <h2>智能助手</h2>
    <div class="conversation" ref="main">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="{ 'userD': msg.isUser, 'botD': !msg.isUser }"
      >
        <div v-show="!msg.isUser"><svg-icon icon-class="chat_robot" style="width: 24px; height: 24px; background-color: #00afff;" /></div>
        <div :class="{ 'user': msg.isUser, 'bot': !msg.isUser }">{{ msg.text }}</div>
        <div v-show="msg.isUser"><svg-icon icon-class="chat_user" style="width: 24px; height: 24px; background-color: #00afff;" /></div>
      </div>
    </div>
    <div class="input-group">
      <textarea ref="text" v-model="userInput" @keydown.enter="keyDownHandle" @input="inputHandle" placeholder="请输入您的问题..." maxlength="10000"
                style="resize: none; height: 96px; min-height: 24px; max-height: 96px;"></textarea>
      <svg-icon v-show="disableChat || loading" icon-class="chat_send" />
      <svg-icon v-show="!disableChat && !loading" icon-class="chat_send_colorful" @click="dialog" style="cursor: hand;" />
    </div>
  </div>
</template>

<script>
//import request from '@/utils/request'
import {getToken} from "@/utils/auth";
import { fetchEventSource, EventStreamContentType } from '@microsoft/fetch-event-source';
import { Notification, MessageBox, Message, Loading } from 'element-ui'
import store from "@/store";

export default {
  data() {
    return {
      sessionId: '',
      userInput: '',
      loading: false,
      disableChat: true,
      isComplete: true,
      messages: [
        // 示例对话初始化，可省略
        { text: "您好，请问有什么可以帮助您的？", isUser: false },
      ],
    };
  },
  methods: {
    inputHandle() {
      this.disableChat = !this.userInput.trim();
      //this.$refs.text.style.height = "auto";

      //this.ttt = "调整前：style.height=" + this.$refs.text.style.height + ", scrollHeight=" + this.$refs.text.scrollHeight;
      /*const maxHeight = parseInt(this.$refs.text.style.maxHeight);
      const minHeight = parseInt(this.$refs.text.style.minHeight);
      if (this.$refs.text.scrollHeight > maxHeight) {
        this.$refs.text.style.height = maxHeight + "px";
      } else if (this.$refs.text.scrollHeight < minHeight) {
        this.$refs.text.style.height = minHeight + "px";
      } else {
        this.$refs.text.style.height = this.$refs.text.scrollHeight + "px";
      }
      //this.ttt = this.ttt + "\n   调整后：style.height=" + this.$refs.text.style.height + ", scrollHeight=" + this.$refs.text.scrollHeight+", lineHeight=" + this.$refs.text.style.lineHeight;

      if (maxHeight < this.$refs.text.scrollHeight) {
        this.$refs.text.style.overflowY = "auto";
      } else {
        this.$refs.text.style.overflowY = "hidden";
      }*/
    },

    keyDownHandle(event) {
      if (!event.shiftKey) {
        // 如果没有按下组合键ctrl，则会阻止默认事件
        event.preventDefault();
        if (!this.loading && !event.isComposing) {
          this.dialog();
        }
      } else {
        // 如果同时按下ctrl+回车键，则会换行
        this.userInput += '\n'
      }
    },

    // 提交对话
    async dialog() {
      if (!this.userInput.trim()) {
        return;
      }
      if (this.loading) {
        return;
      }

      const text = this.userInput;
      this.messages.push({ text: this.userInput, isUser: true });
      this.userInput = ''; // 清空输入框
      this.inputHandle();
      const thisObj = this;

      const ctrl = new AbortController() // 创建AbortController实例，以便中止请求
      const source = await fetchEventSource(process.env.VUE_APP_BASE_API + '/chat/dialog', {
        method: 'POST',
        headers: {
          isToken: true,
          'Authorization': 'Bearer ' + getToken(), // 让每个请求携带自定义token 请根据实际情况自行修改
          'Content-Type': 'application/json',
          'Cache-Control': 'no-cache'
        },
        body: JSON.stringify({
          question: text
        }),
        openWhenHidden: true, // 取消visibilityChange事件
        signal: ctrl.signal, // AbortSignal
        async onopen(response) {
          if (response.ok) {
            return; //一切正常
          } else if(response.status == 401) {
            MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
              store.dispatch('LogOut').then(() => {
                location.href = '/index';
              })
            });
          } else {
            Promise.reject(new Error(JSON.stringify({
              status: response.status,
              message: response.statusText
            })));
          }
        },
        async onmessage(ev) {
          const data = JSON.parse(ev.data);
          //console.log(data)
          let text = data.text;
          if (!text.trim()) {
            return;
          }
          if (!thisObj.loading) {
            thisObj.messages.push({ text: text, isUser: false });
            thisObj.loading = true;
          } else {
            if (data.increment) {
              text = thisObj.messages[thisObj.messages.length - 1].text + text;
            }
            thisObj.$set(thisObj.messages, thisObj.messages.length - 1, { text: text, isUser: false });
          }

          thisObj.$refs.main.scrollTo({top: thisObj.$refs.main.scrollHeight, behavior: 'smooth'});
        },
        onclose() {
          thisObj.loading = false;
          ctrl.abort();
        },
        onerror(err) {
          thisObj.loading = false;
          ctrl.abort();
          this.close();
        }
      });
    },
  },
};
</script>

<style scoped>
.smart-dialog {
  margin: 10px auto;
  border: 1px solid #ccc;
  padding: 10px 20px;
}

.conversation {
  overflow-y: auto;
  height: 400px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 10px;
}

.userD {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 5px;
  border-radius: 5px;
}

.user {
  text-align: right;
  background-color: #f9f9f9;
  padding: 5px 10px;
  margin-bottom: 5px;
  margin-right: 5px;
  border-radius: 5px;
}

.botD {
  display: flex;
  text-align: left;
  margin-bottom: 5px;
  border-radius: 5px;
}

.bot {
  text-align: left;
  background-color: #e6f2ff;
  padding: 5px 10px;
  margin-bottom: 5px;
  margin-left: 5px;
  border-radius: 5px;
}

.input-group {
  display: flex;
  vertical-align: bottom;
}

.input-group textarea {
  background: transparent !important;
  box-shadow: none !important;
  caret-color: #26244c !important;
  color: #26244c !important;
  font-size: 16px;
  line-height: normal;
  line-height: 24px;
  min-height: 28px;
  outline: 0;
  padding: 0 8px 0 0;
  resize: none;
  transition: none !important;
  width: 100%;
}

.input-group button {
  padding: 5px 10px;
}
.input-group svg {
  width: 20px;
  height: 20px;
}
</style>
