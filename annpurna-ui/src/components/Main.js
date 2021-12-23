import React, { Component } from 'react'
import WalletComponent from './common/WalletComponent'


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
          <WalletComponent></WalletComponent>
        </div>
      </div>
      );
    }
}
export default Main;