<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24" style="padding-left: 20px">
        <h2>灵动数上科技</h2>
        <p>
          灵动数上科技有限公司成立于2023年6月，是一家致力于通过大数据、人工智能、云计算、互联网等技术，帮助企业完成AI+业务创新及落地的高科技企业。致力于为企业级客户提供全流程的“AI+”解决方案，旨在通过人工智能技术赋能各行各业，提升企业运营效率和服务质量。
        </p>
        <p>
          公司的核心创始团队成员均来自国内外的知名科技公司，如旷视科技、百度、阿里巴巴等。团队在人工智能领域有着深厚的技术积累和丰富的实践经验，在多模态生成式大模型技术方面具有独特的竞争优势，成为国内不多的掌握该核心技术的AI公司之一。
        </p>
        <p>
          公司坚持以技术创新为驱动，不断推动人工智能技术的发展和应用。不仅能够有效理解和生成文本、图像、视频等多种形式的数据，还能够进行复杂的推理和决策，为企业提供更加智能化的服务。公司通过深度学习、自然语言处理、计算机视觉、大模型等前沿技术，为客户打造定制化的智能解决方案，帮助他们在激烈的市场竞争中保持领先地位。
        </p>
        <p>
          公司的服务已经深入到政府、能源、医疗、消费零售、金融、教育等多个行业。与各行业的企业合作，共同探索AI技术在不同场景下的应用潜力，为客户提供强大的AI能力和深度场景化智能服务。通过与客户的紧密合作，公司也不断优化产品和服务，确保技术能够真正落地，为客户创造实际价值。
        </p>
        <p>
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('http://www.aileap.chat/')"
          >访问主页</el-button
          >
        </p>
      </el-col>

    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
              href="http://www.aileap.chat/"
              target="_blank"
            >http://www.aileap.chat/</el-link
            >
            </p>
            <p>
              <i class="el-icon-user-solid"></i> 联系人：王恺
            </p>
            <p>
              <i class="el-icon-chat-dot-round"></i> 微信：<a
              href="javascript:;"
            >灵动数上科技</a
            >
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="v1.0.0 - 2024-07-08">
              <ol>
                <li>大模型演示系统正式发布</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>待补充</span>
          </div>
          <div class="body">
            ……
            <span style="display: inline-block; height: 30px; line-height: 30px"
            >灵动数上科技</span
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.0.0"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
