import React, { Component } from 'react'
import WalletComponent from './common/WalletComponent'
import TabViewWalletComponent from "./common/TabViewWalletComponent"


class Main extends Component {

  
  constructor(props) {
    super(props);
    this.state = {
      account : '',
      loading : true,
      secret: null
    };
   
  }

 
  render() {

    return (
      <div className="container">
        <div id='content' className='mt-3'> 
          <TabViewWalletComponent></TabViewWalletComponent> 
        </div>
      </div>
      );
    }
}
export default Main;