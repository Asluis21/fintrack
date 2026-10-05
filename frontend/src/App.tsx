import { useState } from 'react'
import './App.css'
import { LoginPage } from './pages/LoginPage';

type CardProps = {
  name:string,
  initialOnline:boolean,
  instances: number
}

function ServerCard ({name, initialOnline, instances} : CardProps){

  const [online, setOnline] = useState(initialOnline);

    return (
      <div>
        <h2>{name}</h2>
        
        <span>{online ? 'Online' : 'Offline'}</span>
        
        {online && <span>{instances} instances</span>}
        
        <button onClick={() => setOnline(!online)}>

          {online ? 'Set offline': 'Set online'}

        </button>
      </div>
    )
}

function Panel({title, children} : {title: string, children : React.ReactNode}){
  return (
    <div>
      <h3>{title}</h3>
      {children}
    </div>
  )
}

function App() {
  
  return (
      
      <div>
        <Panel title='Title test'>
          <ServerCard name='title' initialOnline={true} instances={2}/>
          <ServerCard name='title 2' initialOnline={false} instances={3}/>
          <ServerCard name='title 3' initialOnline={true} instances={4}/>
        </Panel>

        <hr />

        <LoginPage></LoginPage>
      </div>
    
    )
}

export default App
